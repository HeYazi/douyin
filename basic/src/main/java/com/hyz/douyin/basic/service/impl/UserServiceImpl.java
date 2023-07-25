package com.hyz.douyin.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyz.douyin.basic.common.ErrorCode;
import com.hyz.douyin.basic.constant.UserConstant;
import com.hyz.douyin.basic.exception.BusinessException;
import com.hyz.douyin.basic.mapper.UserMapper;
import com.hyz.douyin.basic.model.entity.LoginUser;
import com.hyz.douyin.basic.model.entity.User;
import com.hyz.douyin.basic.model.vo.UserLoginVO;
import com.hyz.douyin.basic.model.vo.UserQueryVO;
import com.hyz.douyin.basic.model.vo.UserRegisterVO;
import com.hyz.douyin.basic.service.UserService;
import com.hyz.douyin.basic.utils.JwtUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author heguande
 * @description 针对表【tb_user(用户表)】的数据库操作Service实现
 * @createDate 2023-07-25 12:43:16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

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
            String token = JwtUtil.createJWT(id.toString());
            // 8. 用户信息脱敏保存至 redis 当中。redis 的 key 为 token，value 为 hash 数据结构的 user 信息映射。
            userToLoginInRedis(user, UserConstant.USER_LOGIN_STATE + token, 30L, TimeUnit.MINUTES);
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
        String token = JwtUtil.createJWT(user.getId().toString());
        // 8. 判断 redis 中是否存在 token，不可重复登录
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(UserConstant.USER_LOGIN_STATE + token))) {
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "重复登录");
        }
        // 9. 用户存在则信息脱敏，保存在 redis 当中
        userToLoginInRedis(user, UserConstant.USER_LOGIN_STATE + token, 30L, TimeUnit.MINUTES);
        // 10. 返回 UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setUserId(user.getId());
        userLoginVO.setStatusCode(0);
        userLoginVO.setStatusMsg("成功");
        return userLoginVO;
    }

    @Override
    public UserQueryVO userQuery(Long userId, String token) {
        // todo 测试
        // 2. Token 在 redis 中判断是否存在
        Map<Object, Object> loginUserMap = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        if (loginUserMap.isEmpty()) {
            //  1. 不存在则抛出异常
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //3. 从 redis 中获取对应的 user 信息
        LoginUser loginUser = BeanUtil.fillBeanWithMap(loginUserMap, new LoginUser(), false);

        //4. todo 在关注表中查询传入的 userId 和自己的 userId 是否有关注关系。目前伪代码默认为 false

        //5. 从 redis 中查询对应的 userId 的数据
        int retryCount = 0;
        while (retryCount < 3) {
            Map<Object, Object> queryLoginUserMap = stringRedisTemplate.opsForHash().entries(UserConstant.USER_INFO_STATE + userId);
            if (!queryLoginUserMap.isEmpty()) {
                //  1. 存在则直接返回 UserQueryVO
                LoginUser queryLoginUser = BeanUtil.fillBeanWithMap(queryLoginUserMap, new LoginUser(), false);
                UserQueryVO userQueryVO = BeanUtil.copyProperties(queryLoginUser, UserQueryVO.class);
                userQueryVO.setIsFollow(false);
                return userQueryVO;
            }

            //6. 不存在则竞争获锁
            RLock lock = redissonClient.getLock(UserConstant.USER_INFO_LOCK + userId);
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
                userToLoginInRedis(queryUser, UserConstant.USER_INFO_STATE + userId, 5L, TimeUnit.MINUTES);
                UserQueryVO userQueryVO = BeanUtil.copyProperties(queryUser, UserQueryVO.class);
                userQueryVO.setIsFollow(false);
                return userQueryVO;
            } finally {
                lock.unlock();
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
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




