package com.hyz.douyin.interaction.model.vo.comment;

import com.hyz.douyin.common.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 评论动作vo
 *
 * @author HYZ
 * @date 2023/8/9 12:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentActionVO extends BaseResponse implements Serializable {

    private static final long serialVersionUID = 6807626990081983150L;

    private CommentVO comment;
}
