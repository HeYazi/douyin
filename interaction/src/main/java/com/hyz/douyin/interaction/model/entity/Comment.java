package com.hyz.douyin.interaction.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * 评论
 *
 * @author HYZ
 * @date 2023/8/8 13:44
 */
@Data
@Document("tb_comment")
public class Comment {
    /**
     * id
     */
    private Long id;
    /**
     *
     */
    private Long userId;
    /**
     * 视频id
     */
    private Long videoId;
    /**
     * 评论文本
     */
    private String commentText;
    /**
     * 创建时间
     */
    private Date createTime;
}
