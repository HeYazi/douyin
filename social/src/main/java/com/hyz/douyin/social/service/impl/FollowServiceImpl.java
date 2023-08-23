package com.hyz.douyin.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.service.InnerUserService;
import com.hyz.douyin.social.model.entity.Follow;
import com.hyz.douyin.social.service.FollowService;
import com.hyz.douyin.social.mapper.FollowMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author heguande
 * @description 针对表【tb_follow(关注表)】的数据库操作Service实现
 * @createDate 2023-08-23 23:16:42
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
        implements FollowService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @DubboReference
    private InnerUserService innerUserService;

    @Override
    public void relationAction(String token, Long toUserId, Integer actionType) {
        // Token 判断
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        if (entries.isEmpty()) {
            //  1. 不存在则抛出异常
            throw new BusinessException(ErrorCode.SOCIAL_OPERATION_ERROR, "您未登录");
        }
        long userId = Long.parseLong((String) entries.get("id"));
        // 对方用户 id 判断，是否存在。修改对方用户的粉丝数量，修改本用户的关注数量
        if (!innerUserService.relationAction(userId, toUserId, actionType)) {
            throw new BusinessException(ErrorCode.SOCIAL_OPERATION_ERROR, "关注操作失败");
        }
        // 生成对应的 Follow，返回结果
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowUserId(toUserId);
        this.save(follow);
    }

    @Override
    public List<UserVO> relationFollowList(String token, Long userId) {
        // id与token判断
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        if (entries.isEmpty()) {
            //  1. 不存在则抛出异常
            throw new BusinessException(ErrorCode.SOCIAL_OPERATION_ERROR, "您未登录");
        }
        if (userId != Long.parseLong((String) entries.get("id"))) {
            throw new BusinessException(ErrorCode.SOCIAL_OPERATION_ERROR, "用户无权限");
        }
        // 返回用户信息列表
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Follow::getUserId, userId);
        List<Long> userIds = new ArrayList<>();
        for (Follow follow : this.list(wrapper)) {
            userIds.add(follow.getFollowUserId());
        }
        return innerUserService.getFollowUserList(userIds);
    }

    @Override
    public List<UserVO> relationFollowerList(String token, Long userId) {
        // id与token判断
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        if (entries.isEmpty()) {
            //  1. 不存在则抛出异常
            throw new BusinessException(ErrorCode.SOCIAL_OPERATION_ERROR, "您未登录");
        }
        if (userId != Long.parseLong((String) entries.get("id"))) {
            throw new BusinessException(ErrorCode.SOCIAL_OPERATION_ERROR, "用户无权限");
        }
        // 返回用户信息列表
        QueryWrapper<Follow> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Follow::getFollowUserId, userId);
        List<Long> userIds = new ArrayList<>();
        for (Follow follow : this.list(wrapper)) {
            userIds.add(follow.getUserId());
        }
        return innerUserService.getFollowerUserList(userIds, token);
    }
}




