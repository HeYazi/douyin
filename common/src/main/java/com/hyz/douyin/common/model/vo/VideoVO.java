package com.hyz.douyin.common.model.vo;

import com.hyz.douyin.common.model.entity.Video;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HYZ
 * @date 2023/8/4 16:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoVO extends Video {

    private static final long serialVersionUID = 4571699048767970509L;

    private UserVO author;
}
