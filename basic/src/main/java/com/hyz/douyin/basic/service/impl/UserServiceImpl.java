package com.hyz.douyin.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyz.douyin.basic.common.BaseResponse;
import com.hyz.douyin.basic.common.ErrorCode;
import com.hyz.douyin.basic.exception.BusinessException;
import com.hyz.douyin.basic.model.entity.User;
import com.hyz.douyin.basic.model.vo.UserRegisterVO;
import com.hyz.douyin.basic.service.UserService;
import com.hyz.douyin.basic.mapper.UserMapper;
import com.hyz.douyin.basic.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author heguande
 * @description 针对表【tb_user(用户表)】的数据库操作Service实现
 * @createDate 2023-07-25 12:43:16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    public static final String SALT = "user";

    @Override
    public UserRegisterVO userRegister(String username, String password) {
        //2. 长度校验
        if (username.length() > 32 || password.length() > 32) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请正确输入账号和密码");
        }
        //3. 字符要求校验（正则表达）只允许 英文大小写+数字
        String regexPattern = "^[a-zA-Z0-9]+$";
        if (!username.matches(regexPattern) || !password.matches(regexPattern)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请正确输入账号和密码");
        }
        //4. 账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User tempUser = this.getOne(queryWrapper);
        if (tempUser != null) {
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "账号已注册");
        }
        //5. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //6. 添加数据
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        user.setName(username);
        if (this.save(user)) {
            Long id = user.getId();
            String token = JwtUtil.createJWT(id.toString());
            UserRegisterVO userRegisterVO = new UserRegisterVO();
            userRegisterVO.setUserId(id);
            userRegisterVO.setToken(token);
            userRegisterVO.setStatusCode(0);
            userRegisterVO.setStatusMsg("注册成功");
        }
        throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "注册失败");
    }
}




