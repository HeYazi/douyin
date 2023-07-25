package com.hyz.douyin.basic.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author HYZ
 * @date 2023/7/25 13:48
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String username;
    private String password;

    private static final long serialVersionUID = 5581100410269008311L;
}
