package com.hyz.douyin.common.service;

/**
 * 内部社交模块
 *
 * @author HYZ
 * @date 2023/8/24 0:45
 */
public interface InnerSocialService {
    /**
     * 是否关注
     *
     * @param userId      用户id
     * @param queryUserId 查询用户id
     * @return {@link Boolean}
     */
    Boolean isFollow(Long userId, Long queryUserId);
}
