package com.hyz.douyin.social.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 粉丝列表
 *
 * @author HYZ
 * @date 2023/8/24 0:09
 */
@Data
public class RelationFollowerListRequest implements Serializable {
    private static final long serialVersionUID = 1109243312855979154L;
    private Long userId;
    private String token;
}
