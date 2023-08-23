package com.hyz.douyin.interaction.controller;

import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.service.InnerUserService;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.interaction.model.dto.comment.CommentActionRequest;
import com.hyz.douyin.interaction.model.dto.comment.CommentListRequest;
import com.hyz.douyin.interaction.model.vo.comment.CommentActionVO;
import com.hyz.douyin.interaction.model.vo.comment.CommentListVO;
import com.hyz.douyin.interaction.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论
 *
 * @author HYZ
 * @date 2023/8/8 13:42
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;
    @DubboReference
    private InnerUserService innerUserService;

    @PostMapping("/action")
    public CommentActionVO commentAction(@RequestBody CommentActionRequest commentActionRequest) {
        // 1. 空参校验
        String token = commentActionRequest.getToken();
        Long videoId = commentActionRequest.getVideoId();
        Integer actionType = commentActionRequest.getActionType();
        ThrowUtils.throwIf(StringUtils.isBlank(token) || videoId == null || actionType == null, ErrorCode.PARAMS_ERROR);
        return commentService.commentAction(commentActionRequest);
    }

    @GetMapping("/list")
    public CommentListVO commentList(CommentListRequest commentListRequest) {
        // 1. 空参校验
        String token = commentListRequest.getToken();
        Long videoId = commentListRequest.getVideoId();
        ThrowUtils.throwIf(StringUtils.isBlank(token) || videoId == null, ErrorCode.PARAMS_ERROR);
        return commentService.commentList(token, videoId);
    }
}
