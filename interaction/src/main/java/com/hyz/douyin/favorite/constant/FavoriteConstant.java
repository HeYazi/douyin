package com.hyz.douyin.favorite.constant;

/**
 * 点赞操作常数
 *
 * @author HYZ
 * @date 2023/7/27 23:27
 */
public interface FavoriteConstant {
    /**
     * 点赞缓存
     */
    String LIKE_STATE = "like:video:";

    /**
     * 消赞缓存
     */
    String UNLIKE_STATE = "unlike:video:";

    /**
     * 最点赞收藏
     */
    String FAVORITE_COLLECTION_NAME = "tb_favorite";
}
