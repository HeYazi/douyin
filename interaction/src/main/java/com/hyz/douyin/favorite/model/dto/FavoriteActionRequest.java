package com.hyz.douyin.favorite.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 赞操作DTO
 *
 * @author HYZ
 * @date 2023/7/27 21:21
 */
@Data
public class FavoriteActionRequest implements Serializable {
    private static final long serialVersionUID = -3580957863277017092L;

    private String token;
    private Long videoId;
    private Integer actionType;
}
