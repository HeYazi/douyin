package com.hyz.douyin.basic.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HYZ
 * @date 2023/7/25 17:14
 */
@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 930378390201801520L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 关注总数
     */
    private Integer followCount;

    /**
     * 粉丝总数
     */
    private Integer followerCount;

    /**
     * 用户个人页顶部大图
     */
    private String backgroundImage;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String signature;

    /**
     * 获赞数量
     */
    private Integer totalFavorited;

    /**
     * 作品数量
     */
    private Integer workCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
