package com.hyz.douyin.user.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author HYZ
 * @date 2023/7/25 20:08
 */
@Data
public class UserQueryRequest implements Serializable {
    private static final long serialVersionUID = -5380551726805629698L;

    private Long userId;
    private String token;
}
