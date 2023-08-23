package com.hyz.douyin.interaction.service;

import com.hyz.douyin.interaction.model.dto.comment.CommentActionRequest;
import com.hyz.douyin.interaction.model.vo.comment.CommentActionVO;
import com.hyz.douyin.interaction.model.vo.comment.CommentListVO;

/**
 * 评论服务
 *
 * @author HYZ
 * @date 2023/7/27 21:17
 */
public interface CommentService {

    /**
     * 评论行动
     *
     * @param commentActionRequest 评论操作请求
     * @return {@link CommentActionVO}
     */
    CommentActionVO commentAction(CommentActionRequest commentActionRequest);


    /**
     * 评论列表
     *
     * @param token   令牌
     * @param videoId 视频id
     * @return {@link CommentListVO}
     */
    CommentListVO commentList(String token, Long videoId);

}
