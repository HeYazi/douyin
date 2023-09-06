package com.hyz.douyin.interaction.service.impl;

import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.model.vo.VideoVO;
import com.hyz.douyin.common.utils.ThrowUtils;
import com.hyz.douyin.interaction.constant.FavoriteConstant;
import com.hyz.douyin.interaction.model.entity.Favorite;
import com.hyz.douyin.interaction.service.FavoriteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hyz.douyin.common.constant.UserConstant.LOGIN_USER_TTL;


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
        String key = UserConstant.USER_LOGIN_STATE + token;
        String idStr = stringRedisTemplate.opsForValue().get(key);
        ThrowUtils.throwIf(StringUtils.isBlank(idStr), ErrorCode.INTERACTION_OPERATION_ERROR, "您未登录");
        long userId = Long.parseLong(idStr);
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // todo 3. 视频是否存在判断（一般来说会在 redis 当中找到对应的数据）  默认为 true

        //  1. 不存在则抛出异常

        // 判断是否存在对应的点赞数据

        // 4. 判断操作
        if (actionType == 1) {
            // 点赞操作
            // 1. 向消赞缓存中删除对应的 userId 的 videoId
            stringRedisTemplate.opsForSet().remove(FavoriteConstant.UNLIKE_STATE + userId, videoId.toString());
            // 2. 将对应的 userId 和 videoId 存放于点赞当中。key 为前缀+userId，value 为 videoIds。
            stringRedisTemplate.opsForSet().add(FavoriteConstant.LIKE_STATE + userId, videoId.toString());
        } else {
            // 消赞操作
            // 2. 如果是取消点赞操作
            // 1. 向点赞缓存中删除对应的 userId 的 videoId
            stringRedisTemplate.opsForSet().remove(FavoriteConstant.LIKE_STATE + userId, videoId.toString());
            // 2. 将对应的 userId 和 videoId 存放于 redis 当中
            stringRedisTemplate.opsForSet().add(FavoriteConstant.UNLIKE_STATE + userId, videoId.toString());
        }
    }

    @Override
    public List<VideoVO> favoriteList(String token, Long userId) {

        // 2. 根据token获取对应的用户信息，判断 tokenUserId 是否等于 userId
        String key = UserConstant.USER_LOGIN_STATE + token;
        String idStr = stringRedisTemplate.opsForValue().get(key);
        ThrowUtils.throwIf(StringUtils.isBlank(idStr), ErrorCode.INTERACTION_OPERATION_ERROR, "您未登录");
        Long id = Long.valueOf(idStr);
        ThrowUtils.throwIf(!Objects.equals(id, userId), ErrorCode.INTERACTION_OPERATION_ERROR, "非本用户获取点赞列表");
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 3. 根据 userId 在 mongodb 中获取对应的 FavoriteList
        List<Long> videoList = new ArrayList<>();
        Criteria criteria = Criteria.where("user_id").is(userId);
        List<Favorite> favoriteList = mongoTemplate.find(Query.query(criteria), Favorite.class);
        favoriteList.forEach((favorite -> {
            videoList.add(favorite.getVideoId());
        }));
        // 4. 在 redis 中获取对应的 FavoriteList
        Set<String> videoSet = stringRedisTemplate.opsForSet().members(FavoriteConstant.LIKE_STATE + userId);
        if (videoSet != null) {
            videoSet.forEach((videoIds) -> {
                Long videoId = Long.valueOf(videoIds);
                videoList.add(videoId);
            });
        }
        // todo  5. 将 FavoriteList 中的 videoIdList 发送给  video 模块，返回对应的 video 信息。现在先返回模拟数据
        UserVO userVO = new UserVO();
        userVO.setId(0L);
        userVO.setName("");
        userVO.setFollowCount(0L);
        userVO.setFollowerCount(0L);
        userVO.setIsFollow(false);
        userVO.setAvatar("");
        userVO.setBackgroundImage("");
        userVO.setSignature("");
        userVO.setTotalFavorited(0L);
        userVO.setWorkCount(0L);
        VideoVO videoVO = new VideoVO();
        videoVO.setAuthor(userVO);
        videoVO.setId(0L);
        videoVO.setAuthorId(0L);
        videoVO.setCommentCount(0L);
        videoVO.setCoverUrl("");
        videoVO.setFavoriteCount(0L);
        videoVO.setPlayUrl("");
        videoVO.setTitle("");

        // 5. 将信息打包封装返回给前端用户
        return Collections.singletonList(videoVO);
    }

}
