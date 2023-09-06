package com.hyz.douyin.common.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户查询VO
 *
 * @author HYZ
 * @date 2023/7/25 20:10
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 9034388933167464611L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 关注总数
     */
    private Long followCount;

    /**
     * 粉丝总数
     */
    private Long followerCount;

    /**
     * 是否关注
     */
    private Boolean isFollow;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户个人页顶部大图
     */
    private String backgroundImage;

    /**
     * 个人简介
     */
    private String signature;

    /**
     * 获赞数量
     */
    private Long totalFavorited;

    /**
     * 作品数量
     */
    private Long workCount;

    /**
     * 点赞数量
     */
    private Long favoriteCount;
}
