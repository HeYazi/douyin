package com.hyz.douyin.user.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 *
 * @author HYZ
 * @TableName tb_user
 * @date 2023/07/25
 */
@TableName(value ="tb_user")
@Data
public class User implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名称 
     */
    private String name;

    /**
     * 关注总数
     */
    private Long followCount;

    /**
     *  粉丝总数
     */
    private Long followerCount;

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
    private Long totalFavorited;

    /**
     * 作品数量
     */
    private Long workCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}