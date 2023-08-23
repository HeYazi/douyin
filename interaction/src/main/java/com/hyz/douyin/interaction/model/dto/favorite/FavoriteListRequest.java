package com.hyz.douyin.interaction.model.dto.favorite;

import lombok.Data;

import java.io.Serializable;

/**
 * 点赞列表DTO
 *
 * @author HYZ
 * @date 2023/7/27 21:24
 */
@Data
public class FavoriteListRequest implements Serializable {
    private static final long serialVersionUID = -1940713510248986640L;

    private Long userId;
    private String token;
}
