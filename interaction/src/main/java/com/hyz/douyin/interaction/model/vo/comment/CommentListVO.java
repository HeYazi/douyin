package com.hyz.douyin.interaction.model.vo.comment;

import com.hyz.douyin.common.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 评论列表签证官
 *
 * @author HYZ
 * @date 2023/8/13 17:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentListVO extends BaseResponse {

    private static final long serialVersionUID = 4430561363777808610L;

    private List<CommentVO> commentList;
}
