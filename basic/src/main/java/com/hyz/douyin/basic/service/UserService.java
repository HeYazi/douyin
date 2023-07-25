package com.hyz.douyin.basic.service;

import com.hyz.douyin.basic.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyz.douyin.basic.model.vo.UserLoginVO;
import com.hyz.douyin.basic.model.vo.UserQueryVO;
import com.hyz.douyin.basic.model.vo.UserRegisterVO;

/**
* @author heguande
* @description 针对表【tb_user(用户表)】的数据库操作Service
* @createDate 2023-07-25 12:43:16
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return {@link UserRegisterVO}
     */
    UserRegisterVO userRegister(String username, String password);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return {@link UserLoginVO}
     */
    UserLoginVO userLogin(String username, String password);

    /**
     * 用户查询
     *
     * @param userId 用户id
     * @param token  令牌
     * @return {@link UserQueryVO}
     */
    UserQueryVO userQuery(Long userId, String token);
}
