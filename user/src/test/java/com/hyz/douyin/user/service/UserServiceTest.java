package com.hyz.douyin.user.service;

import com.hyz.douyin.user.model.vo.UserLoginVO;
import com.hyz.douyin.user.model.vo.UserRegisterVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author HYZ
 * @date 2023/7/25 16:34
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        UserRegisterVO userRegisterVO = userService.userRegister("hyz", "123456");
        System.out.println(userRegisterVO);
    }

    @Test
    void userLogin() {
        UserLoginVO hyz = userService.userLogin("hyz", "123456");
        System.out.println(hyz);
    }
}