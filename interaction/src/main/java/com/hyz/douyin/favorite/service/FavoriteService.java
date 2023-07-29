package com.hyz.douyin.favorite.service;

import com.hyz.douyin.common.common.BaseResponse;

/**
 * 点赞服务
 *
 * @author HYZ
 * @date 2023/7/27 21:17
 */
public interface FavoriteService {

    /**
     * 点赞动作
     *
     * @param token      令牌
     * @param videoId    视频id
     * @param actionType 动作类型
     */
    void favoriteAction(String token, Long videoId, Integer actionType);
}
