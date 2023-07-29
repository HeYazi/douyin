package com.hyz.douyin.favorite.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.favorite.constant.FavoriteConstant;
import com.hyz.douyin.favorite.service.FavoriteService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.Map;

/**
 * 点赞服务服务实现类
 *
 * @author HYZ
 * @date 2023/7/27 21:17
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void favoriteAction(String token, Long videoId, Integer actionType) {
        // 2. 从 redis 中根据对应的 token 获取用户数据，判断用户是否存在
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        if (entries.isEmpty()) {
            //  1. 不存在则抛出异常
            throw new BusinessException(ErrorCode.INTERACTION_OPERATION_ERROR, "您未登录");
        }
        Long userId = (Long) entries.get("id");

        //
        // todo 3. 视频是否存在判断（一般来说会在 redis 当中找到对应的数据）  默认为 true
        //  1. 不存在则抛出异常

        // 4. 判断操作
        if (actionType == 1) {
            // 1. 向消赞缓存中删除对应的 userId 的 videoId
            stringRedisTemplate.opsForSet().remove(FavoriteConstant.UNLIKE_STATE + userId, videoId.toString());
            // 2. 将对应的 userId 和 videoId 存放于点赞当中。key 为前缀+userId，value 为 videoIds。
            stringRedisTemplate.opsForSet().add(FavoriteConstant.LIKE_STATE + userId, videoId.toString());
        } else {
            // 2. 如果是取消点赞操作
            // 1. 向点赞缓存中删除对应的 userId 的 videoId
            stringRedisTemplate.opsForSet().remove(FavoriteConstant.LIKE_STATE + userId, videoId.toString());
            // 2. 将对应的 userId 和 videoId 存放于 redis 当中
            stringRedisTemplate.opsForSet().add(FavoriteConstant.UNLIKE_STATE + userId, videoId.toString());
        }
    }
}
