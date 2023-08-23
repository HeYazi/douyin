package com.hyz.douyin.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.service.InnerSocialService;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.user.mapper.UserMapper;
import com.hyz.douyin.user.model.entity.LoginUser;
import com.hyz.douyin.user.model.entity.User;
import com.hyz.douyin.user.model.vo.UserLoginVO;
import com.hyz.douyin.user.model.vo.UserRegisterVO;
import com.hyz.douyin.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.hyz.douyin.user.common.UserConstant.*;

/**
 * @author heguande
 * @description 针对表【tb_user(用户表)】的数据库操作Service实现
 * @createDate 2023-07-25 12:43:16
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @DubboReference
    private InnerSocialService innerSocialService;

    private final String SALT = "user";


    @Override
    @Transactional
    public UserRegisterVO userRegister(String username, String password) {
        checkUser(username, password);
        // 4. 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User tempUser = this.getOne(queryWrapper);
        if (tempUser != null) {
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "账号已注册");
        }
        // 5. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 6. 添加数据
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        user.setName(username);
        if (this.save(user)) {
            // 7. 生成 token
            Long id = user.getId();
            String token = JWTUtil.createToken(USER_TOKEN_MAP, String.valueOf(id).getBytes());
            // 8. 用户信息脱敏保存至 redis 当中。redis 的 key 为 token，value 为 hash 数据结构的 user 信息映射。
            userToLoginInRedis(user, UserConstant.USER_LOGIN_STATE + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
            // 9. 返回 UserRegisterVO
            UserRegisterVO userRegisterVO = new UserRegisterVO();
            userRegisterVO.setUserId(id);
            userRegisterVO.setToken(token);
            userRegisterVO.setStatusCode(0);
            userRegisterVO.setStatusMsg("注册成功");
            return userRegisterVO;
        }
        throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "注册失败");
    }

    @Override
    public UserLoginVO userLogin(String username, String password) {
        checkUser(username, password);
        // 4. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 5. 账号+密码查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username)
                .eq(User::getPassword, encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            // 6 用户不存在则报错
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "账号或密码错误");
        }
        // 7. 获取token
        Long id = user.getId();
        String token = JWTUtil.createToken(USER_TOKEN_MAP, String.valueOf(id).getBytes());
        // 8. 判断 redis 中是否存在 token
        String key = UserConstant.USER_LOGIN_STATE + token;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            // 存在则重置时间
            stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        } else {
            // 9. 用户存在则信息脱敏，保存在 redis 当中
            userToLoginInRedis(user, UserConstant.USER_LOGIN_STATE + token, LOGIN_USER_TTL, TimeUnit.MINUTES);
        }
        // 10. 返回 UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setUserId(user.getId());
        userLoginVO.setStatusCode(0);
        userLoginVO.setStatusMsg("成功");
        return userLoginVO;
    }

    /**
     * 用户查询
     *
     * @param id     查询人id
     * @param userId 被查人id
     * @return {@link UserVO}
     */
    @Override
    public UserVO userQuery(Long id, Long userId) {

        //5. 从 redis 中获取被查询用户的 user 的信息
        int retryCount = 0;
        RLock lock = redissonClient.getLock(USER_INFO_LOCK + userId);
        while (retryCount < 3) {
            Map<Object, Object> queryLoginUserMap = stringRedisTemplate.opsForHash().entries(USER_INFO_STATE + userId);
            if (!queryLoginUserMap.isEmpty()) {
                //  1. 存在则直接返回 UserQueryVO
                // 缓存加时
                stringRedisTemplate.expire(USER_INFO_STATE + userId, QUERY_USER_TTL, TimeUnit.MINUTES);
                LoginUser queryLoginUser = BeanUtil.fillBeanWithMap(queryLoginUserMap, new LoginUser(), false);
                UserVO userVO = BeanUtil.copyProperties(queryLoginUser, UserVO.class);
                if (id == null) {
                    userVO.setIsFollow(false);
                } else {
                    // 关注模块，判断是否关注
                    userVO.setIsFollow(innerSocialService.isFollow(id, userId));
                }
                return userVO;
            }
            //6. 用户信息不存在则竞争获锁
            boolean b = lock.tryLock();
            if (!b) {
                // 失败则休眠 1s 后再次去 redis 查询，依然不存在则重复步骤6
                try {
                    TimeUnit.SECONDS.sleep(1L);
                    retryCount++;
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                // 7. 成功则向 mysql 中查询对应的数据，而后重建缓存，返回 UserQueryVO
                User queryUser = this.getById(userId);
                ThrowUtils.throwIf(queryUser == null, ErrorCode.USER_OPERATION_ERROR, "用户不存在");
                userToLoginInRedis(queryUser, USER_INFO_STATE + userId, QUERY_USER_TTL, TimeUnit.MINUTES);
                UserVO userVO = BeanUtil.copyProperties(queryUser, UserVO.class);
                if (id == null) {
                    userVO.setIsFollow(false);
                } else {
                    // 关注模块，判断是否关注
                    userVO.setIsFollow(innerSocialService.isFollow(id, userId));
                }
                return userVO;
            } finally {
                lock.unlock();
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }


    @Override
    public UserVO userQuery(Long userId, String token) {
        // 2. Token 在 redis 中判断是否存在
        Map<Object, Object> loginUserMap = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        if (loginUserMap.isEmpty()) {
            //  1. 不存在则抛出异常
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //3. 从 redis 中获取查询用户的 user 信息
        LoginUser loginUser = BeanUtil.fillBeanWithMap(loginUserMap, new LoginUser(), false);

        if (Objects.equals(userId, loginUser.getId())) {
            UserVO userVO = BeanUtil.copyProperties(loginUser, UserVO.class);
            userVO.setIsFollow(true);
            return userVO;
        }
        return userQuery(loginUser.getId(), userId);

    }

    /**
     * 参数校验
     *
     * @param username 用户名
     * @param password 密码
     */
    private void checkUser(String username, String password) {
        //2. 长度校验
        if (username.length() > 32 || password.length() > 32 || username.length() < 3 || password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请正确输入账号和密码");
        }
        //3. 字符要求校验（正则表达）只允许 英文大小写+数字
        String regexPattern = "^[a-zA-Z0-9]+$";
        if (!username.matches(regexPattern) || !password.matches(regexPattern)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请正确输入账号和密码");
        }
    }

    /**
     * user 转换为 loginUser 并存放在 redis 当中
     *
     * @param user     用户
     * @param key      key
     * @param timeout  超时
     * @param timeUnit 时间单位
     */
    private void userToLoginInRedis(User user, String key, Long timeout, TimeUnit timeUnit) {
        LoginUser loginUser = BeanUtil.copyProperties(user, LoginUser.class);
        Map<String, Object> map = BeanUtil.beanToMap(loginUser, new HashMap<>(1),
                CopyOptions.create()
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue == null) {
                                fieldValue = "";
                            } else {
                                fieldValue = String.valueOf(fieldValue);
                            }
                            return fieldValue;
                        })
                        .setIgnoreNullValue(true));
        stringRedisTemplate.opsForHash().putAll(key, map);
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }
}