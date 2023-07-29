package com.hyz.douyin.user.model.vo;

import com.hyz.douyin.common.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户注册签证官
 * 用户注册响应
 *
 * @author HYZ
 * @date 2023/7/25 13:57
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class UserRegisterVO extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -9138956492395412710L;

    private String token;
    private Long userId;
}
