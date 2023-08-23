package com.hyz.douyin.social.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 关注操作
 *
 * @author HYZ
 * @date 2023/8/23 23:23
 */
@Data
public class RelationActionRequest implements Serializable {
    private static final long serialVersionUID = -3301138536639234098L;
    private String token;
    private Long toUserId;
    private Integer actionType;
}
