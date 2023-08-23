package com.hyz.douyin.interaction.service;

import com.hyz.douyin.common.model.vo.VideoVO;

import java.util.List;

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

    /**
     * 最喜欢列表
     *
     * @param token  令牌
     * @param userId 用户id
     * @return {@link List}<{@link VideoVO}>
     */
    List<VideoVO> favoriteList(String token, Long userId);
}
