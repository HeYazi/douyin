package com.hyz.douyin.basic.controller;

import com.hyz.douyin.basic.common.ErrorCode;
import com.hyz.douyin.basic.model.dto.UserRegisterRequest;
import com.hyz.douyin.basic.model.vo.UserRegisterVO;
import com.hyz.douyin.basic.service.UserService;
import com.hyz.douyin.basic.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * user 控制层
 *
 * @author HYZ
 * @date 2023/7/25 13:55
 */
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public UserRegisterVO userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 1. 空参校验
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        ThrowUtils.throwIf(StringUtils.isAllBlank(username, password), ErrorCode.PARAMS_ERROR, "请正确输入用户名和密码");
        return userService.userRegister(username, password);
    }
}
