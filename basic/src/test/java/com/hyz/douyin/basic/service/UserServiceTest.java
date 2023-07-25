package com.hyz.douyin.basic.service;

import com.hyz.douyin.basic.model.vo.UserLoginVO;
import com.hyz.douyin.basic.model.vo.UserRegisterVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

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