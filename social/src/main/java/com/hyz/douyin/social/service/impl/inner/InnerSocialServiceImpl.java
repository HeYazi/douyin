package com.hyz.douyin.social.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyz.douyin.common.service.InnerSocialService;
import com.hyz.douyin.social.model.entity.Follow;
import com.hyz.douyin.social.service.FollowService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HYZ
 * @date 2023/8/24 0:50
 */
@Service
@DubboService
public class InnerSocialServiceImpl implements InnerSocialService {
    @Resource
    private FollowService followService;

    @Override
    public Boolean isFollow(Long userId, Long queryUserId) {
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, queryUserId);
        Follow one = followService.getOne(wrapper);
        return one != null;
    }
}
