package com.hyz.douyin.interaction.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论列表请求
 *
 * @author HYZ
 * @date 2023/8/13 17:01
 */
@Data
public class CommentListRequest implements Serializable {
    private static final long serialVersionUID = 2092719557675633138L;
    /**
     * 令牌
     */
    private String token;
    /**
     * 视频id
     */
    private Long videoId;
}
