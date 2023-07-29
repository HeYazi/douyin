package com.hyz.douyin.favorite.model.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 视频点赞表
 *
 * @author HYZ
 * @date 2023/7/27 21:13
 */
@Data
@Document("tb_favorite")
public class Favorite {
    private Long id;
    private Long userId;
    private Long videoId;
}
