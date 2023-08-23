package com.hyz.douyin.interaction.model.vo.comment;

import com.hyz.douyin.common.model.vo.UserVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HYZ
 * @date 2023/8/9 12:17
 */
@Data
public class CommentVO implements Serializable {

    private static final long serialVersionUID = -1545104528192005564L;
    /**
     * id
     */
    private Long id;
    /**
     * 用户
     */
    private UserVO user;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建日期
     */
    private String createDate;
}
