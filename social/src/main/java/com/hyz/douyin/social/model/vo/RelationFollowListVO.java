package com.hyz.douyin.social.model.vo;

import com.hyz.douyin.common.common.BaseResponse;
import com.hyz.douyin.common.model.vo.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 关注列表VO
 *
 * @author HYZ
 * @date 2023/8/24 0:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RelationFollowListVO extends BaseResponse implements Serializable {
    private static final long serialVersionUID = -6058672053119638856L;
    private List<UserVO> userList;
}
