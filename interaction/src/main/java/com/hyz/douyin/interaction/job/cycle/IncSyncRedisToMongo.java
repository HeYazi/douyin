package com.hyz.douyin.interaction.job.cycle;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.exception.BusinessException;
import com.hyz.douyin.common.service.InnerUserService;
import com.hyz.douyin.interaction.constant.CollectionConstant;
import com.hyz.douyin.interaction.constant.CommentConstant;
import com.hyz.douyin.interaction.constant.FavoriteConstant;
import com.hyz.douyin.interaction.model.entity.Comment;
import com.hyz.douyin.interaction.model.entity.Favorite;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 增量同步缓存到 mongodb 中
 *
 * @author HYZ
 * @date 2023/07/28
 */
@Component
@Slf4j
public class IncSyncRedisToMongo {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MongoTemplate mongoTemplate;
    @DubboReference
    private InnerUserService innerUserService;

    /**
     * 同步缓存到mongo
     *
     * @param prefixKey 前缀key
     */
    public Map<String, Set<String>> syncCacheToMongo(String prefixKey) {
        Set<String> keys = stringRedisTemplate.keys(prefixKey + "*");
        if (keys != null) {
            Map<String, Set<String>> map = new HashMap<>();
            for (String key : keys) {
                Set<String> members = stringRedisTemplate.opsForSet().members(key);
                if (members != null) {
                    map.put(key.substring(prefixKey.length()), members);
                }
                stringRedisTemplate.delete(key);
            }
            return map;
        }
        return null;
    }

    /**
     * 点赞列表同步到mongo当中
     */
    public void synLikeToMongo() {
        // 从点赞缓存中拿到对应的数据
        Map<String, Set<String>> map = syncCacheToMongo(FavoriteConstant.LIKE_STATE);
        if (map != null) {
            for (String mapKey : map.keySet()) {
                for (String s : map.get(mapKey)) {
                    // 获取 userId 和 videoId
                    Long userId = Long.parseLong(mapKey);
                    Long videoId = Long.parseLong(s);
                    // todo 将 videoId 传给视频模块，视频模块返回对应的 userId，下面用模拟数据实现
                    Long videoOwnerId = 1L;
                    if (!innerUserService.updateTotalFavorited(1, videoOwnerId)) {
                        throw new BusinessException(ErrorCode.INTERACTION_OPERATION_ERROR, "修改用户点赞数的用户不存在");
                    }
                    // 先查询判断是否存在对应的数据
                    Criteria criteria = Criteria.where("user_id").is(userId).and("video_id").is(videoId);
                    Favorite one = mongoTemplate.findOne(Query.query(criteria), Favorite.class, CollectionConstant.FAVORITE_COLLECTION_NAME);
                    // 不存在对应的数据则插入
                    List<Favorite> list = new ArrayList<>();
                    if (one == null) {
                        Long id = IdUtil.getSnowflakeNextId();
                        Favorite favorite = new Favorite();
                        favorite.setId(id);
                        favorite.setUserId(userId);
                        favorite.setVideoId(videoId);
                        list.add(favorite);
//                        mongoTemplate.save(favorite, FavoriteConstant.FAVORITE_COLLECTION_NAME);
                    }
                    mongoTemplate.insertAll(list);
                }
            }
        }
    }

    /**
     * 消赞列表同步到mongo当中
     */
    public void synUnLikeToMongo() {
        // 从消赞缓存中拿到对应的数据
        Map<String, Set<String>> map = syncCacheToMongo(FavoriteConstant.UNLIKE_STATE);
        if (map != null) {
            for (String userId : map.keySet()) {
                Set<String> set = map.get(userId);
                ArrayList<Long> longs = new ArrayList<>();
                for (String s : set) {
                    longs.add(Long.parseLong(s));
                    // todo 将 videoId 传给视频模块，视频模块返回对应的 userId，下面用模拟数据实现
                    Long videoOwnerId = 1L;
                    if (!innerUserService.updateTotalFavorited(-1, videoOwnerId)) {
                        throw new BusinessException(ErrorCode.INTERACTION_OPERATION_ERROR, "修改用户点赞数的用户不存在");
                    }
                }
                mongoTemplate.remove(Query.query(
                        Criteria.where("user_id")
                                .is(Long.parseLong(userId))
                                .and("video_id")
                                .in(longs)), Favorite.class);
            }
        }
    }

    public void synSaveCommentToMongo() {
        Set<String> keys = stringRedisTemplate.keys(CommentConstant.SAVE_COMMENT + "*");
        if (keys != null) {
            ArrayList<Comment> comments = new ArrayList<>();
            for (String key : keys) {
                String commentSave = stringRedisTemplate.opsForValue().getAndDelete(key);
                Comment comment = JSONUtil.toBean(commentSave, Comment.class);
                comments.add(comment);
            }
            mongoTemplate.insertAll(comments);
        }
    }

    public void synDeleteCommentToMongo() {
        Set<String> keys = stringRedisTemplate.keys(CommentConstant.DELETE_COMMENT + "*");
        if (keys != null) {

            for (String key : keys) {
                String userIdStr = stringRedisTemplate.opsForValue().getAndDelete(key);
                if (userIdStr == null) {
                    throw new BusinessException(ErrorCode.INTERACTION_OPERATION_ERROR, "删除评论异常");
                }
                String[] split = key.split(":");
                Long userId = Long.valueOf(userIdStr);
                Long videoId = Long.valueOf(split[2]);
                Long commentId = Long.valueOf(split[3]);
                mongoTemplate.remove(Query.query(
                        Criteria.where("id")
                                .is(commentId)
                                .and("user_id")
                                .is(userId)
                                .and("video_id")
                                .is(videoId)), Comment.class);
            }
        }
    }

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 1000 * 6)
    public void run() {
//        log.info("定时任务启动");
        synLikeToMongo();
        synUnLikeToMongo();
        synSaveCommentToMongo();
        synDeleteCommentToMongo();
    }
}