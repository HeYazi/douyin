package com.hyz.douyin.interaction.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论操作请求
 *
 * @author HYZ
 * @date 2023/8/8 22:08
 */
@Data
public class CommentActionRequest implements Serializable {
    private static final long serialVersionUID = -3691048799664707743L;
    /**
     * 令牌
     */
    private String token;
    /**
     * 视频id
     */
    private Long videoId;
    /**
     * 动作类型
     */
    private Integer actionType;
    /**
     * 评论文本
     */
    private String commentText;
    /**
     * 评论id
     */
    private Long commentId;
}
