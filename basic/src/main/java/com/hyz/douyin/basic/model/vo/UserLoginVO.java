package com.hyz.douyin.basic.model.vo;

import com.hyz.douyin.basic.common.BaseResponse;
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

public class UserLoginVO extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -9138956492395412710L;

    private String token;
    private Long userId;
}
