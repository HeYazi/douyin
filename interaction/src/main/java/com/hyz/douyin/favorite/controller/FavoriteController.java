package com.hyz.douyin.favorite.controller;


import com.hyz.douyin.common.common.BaseResponse;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.favorite.model.dto.FavoriteActionRequest;
import com.hyz.douyin.favorite.service.FavoriteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 视频点赞控制层
 *
 * @author HYZ
 * @date 2023/7/27 21:13
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Resource
    private FavoriteService favoriteService;

    @PostMapping("/action")
    public BaseResponse favoriteAction(@RequestBody FavoriteActionRequest favoriteActionRequest) {
        // 1. 空参判断
        String token = favoriteActionRequest.getToken();
        Long videoId = favoriteActionRequest.getVideoId();
        Integer actionType = favoriteActionRequest.getActionType();
        ThrowUtils.throwIf(
                StringUtils.isAllBlank(token) || videoId == null || actionType == null,
                ErrorCode.PARAMS_ERROR
        );
        favoriteService.favoriteAction(token, videoId, actionType);
        return new BaseResponse(0, "成功");
    }
}
