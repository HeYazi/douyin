package com.hyz.douyin.favorite.job.cycle;

import cn.hutool.core.util.IdUtil;
import com.hyz.douyin.favorite.constant.FavoriteConstant;
import com.hyz.douyin.favorite.model.entity.Favorite;
import lombok.extern.slf4j.Slf4j;
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
                    // 先查询判断是否存在对应的数据
                    Criteria criteria = Criteria.where("user_id").is(userId).and("video_id").is(videoId);
                    Favorite one = mongoTemplate.findOne(Query.query(criteria), Favorite.class, FavoriteConstant.FAVORITE_COLLECTION_NAME);
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
                mongoTemplate.remove(Query.query(Criteria.where("user_id").is(userId).and("video_id").in(set)));
            }
        }
    }

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 1000)
    public void run() {
        synLikeToMongo();
    }
}