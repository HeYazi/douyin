package com.hyz.douyin.common.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 视频实体类
 *
 * @author HYZ
 * @date 2023/8/4 16:39
 */
@Data
public class Video implements Serializable {

    private static final long serialVersionUID = -120592930864213337L;

    /**
     * 视频唯一标识
     */
    private long id;
    /**
     * 用户id
     */
    private long authorId;
    /**
     * 视频的评论总数
     */
    private long commentCount;
    /**
     * 视频封面地址
     */
    private String coverUrl;
    /**
     * 视频的点赞总数
     */
    private long favoriteCount;
    /**
     * 视频播放地址
     */
    private String playUrl;
    /**
     * 视频标题
     */
    private String title;
}
