package com.hyz.douyin.interaction.controller;


import com.hyz.douyin.common.common.BaseResponse;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.model.vo.VideoVO;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.interaction.model.dto.favorite.FavoriteActionRequest;
import com.hyz.douyin.interaction.model.dto.favorite.FavoriteListRequest;
import com.hyz.douyin.interaction.model.vo.favorite.FavoriteListVO;
import com.hyz.douyin.interaction.service.FavoriteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/list")
    public FavoriteListVO favoriteList(@RequestBody FavoriteListRequest favoriteListRequest) {
        // 1. 空参判断
        Long userId = favoriteListRequest.getUserId();
        String token = favoriteListRequest.getToken();
        ThrowUtils.throwIf(
                StringUtils.isBlank(token) || userId == null,
                ErrorCode.PARAMS_ERROR
        );

        List<VideoVO> videoList = favoriteService.favoriteList(token, userId);
        FavoriteListVO favoriteListVO = new FavoriteListVO();
        favoriteListVO.setVideoList(videoList);
        favoriteListVO.setStatusCode(0);
        favoriteListVO.setStatusMsg("成功");

        return favoriteListVO;
    }
}
