package com.hyz.douyin.interaction.model.vo.favorite;

import com.hyz.douyin.common.common.BaseResponse;
import com.hyz.douyin.common.model.vo.VideoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 喜欢视频列表返回体
 *
 * @author HYZ
 * @date 2023/8/4 12:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FavoriteListVO extends BaseResponse {

    private static final long serialVersionUID = -1046304784235690532L;

    List<VideoVO> videoList;
}
