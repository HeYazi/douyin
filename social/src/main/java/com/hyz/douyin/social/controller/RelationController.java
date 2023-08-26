package com.hyz.douyin.social.controller;

import com.hyz.douyin.common.common.BaseResponse;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.social.model.dto.RelationActionRequest;
import com.hyz.douyin.social.model.dto.RelationFollowListRequest;
import com.hyz.douyin.social.model.dto.RelationFollowerListRequest;
import com.hyz.douyin.social.model.vo.RelationFollowListVO;
import com.hyz.douyin.social.service.FollowService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HYZ
 * @date 2023/8/23 23:22
 */
@RestController
@RequestMapping("/relation")
public class RelationController {
    @Resource
    private FollowService followService;

    @PostMapping("/action")
    public BaseResponse relationAction(@RequestBody RelationActionRequest relationActionRequest) {
        String token = relationActionRequest.getToken();
        Long toUserId = relationActionRequest.getToUserId();
        Integer actionType = relationActionRequest.getActionType();
        ThrowUtils.throwIf(StringUtils.isBlank(token) || !ObjectUtils.allNotNull(toUserId, actionType), ErrorCode.PARAMS_ERROR);
        followService.relationAction(token, toUserId, actionType);
        return new BaseResponse(0, "成功");
    }

    @GetMapping("/follow/list")
    public RelationFollowListVO relationFollowList(@RequestBody RelationFollowListRequest relationFollowListRequest) {
        Long userId = relationFollowListRequest.getUserId();
        String token = relationFollowListRequest.getToken();
        ThrowUtils.throwIf(StringUtils.isBlank(token) || ObjectUtils.allNull(userId), ErrorCode.PARAMS_ERROR);
        List<UserVO> userList = followService.relationFollowList(token, userId);
        RelationFollowListVO relationFollowListVO = new RelationFollowListVO();
        relationFollowListVO.setStatusCode(0);
        relationFollowListVO.setStatusMsg("成功");
        relationFollowListVO.setUserList(userList);
        return relationFollowListVO;
    }

    @GetMapping("/follower/list")
    public RelationFollowListVO relationFollowList(@RequestBody RelationFollowerListRequest relationFollowerListRequest) {
        Long userId = relationFollowerListRequest.getUserId();
        String token = relationFollowerListRequest.getToken();
        ThrowUtils.throwIf(StringUtils.isBlank(token) || ObjectUtils.allNull(userId), ErrorCode.PARAMS_ERROR);
        List<UserVO> userList = followService.relationFollowerList(token, userId);
        RelationFollowListVO relationFollowListVO = new RelationFollowListVO();
        relationFollowListVO.setStatusCode(0);
        relationFollowListVO.setStatusMsg("成功");
        relationFollowListVO.setUserList(userList);
        return relationFollowListVO;
    }
}
