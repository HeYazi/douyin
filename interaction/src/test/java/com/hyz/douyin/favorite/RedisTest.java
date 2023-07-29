package com.hyz.douyin.favorite;

import cn.hutool.core.lang.UUID;
import com.hyz.douyin.favorite.constant.FavoriteConstant;
import com.hyz.douyin.favorite.model.entity.Favorite;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author HYZ
 * @date 2023/7/28 9:27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testRedisSet() {
        long userId = 1L;
        long videoId1 = 1L;
        long videoId2 = 2L;
        long videoId3 = 3L;
        stringRedisTemplate.opsForSet().add("like:cache:" + userId, Long.toString(videoId1), Long.toString(videoId2), Long.toString(videoId3));
        Set<String> members = stringRedisTemplate.opsForSet().members("like:cache:" + userId);
        System.out.println(members);
    }

    @Test
    public void testKeys() {
        for (int i = 0; i < 10; i++) {
            stringRedisTemplate.opsForSet().add("like:cache:" + i, String.valueOf(i));
        }
        Set<String> keys = stringRedisTemplate.keys("like:cache:*");
        if (keys != null) {
            for (String key : keys) {
                Set<String> videoIds = stringRedisTemplate.opsForSet().members(key);
                Favorite favorite = new Favorite();

            }
        }
    }

    @Test
    public void testDelete() {
//        stringRedisTemplate.delete(FavoriteConstant.UNLIKE_STATE + 99);
        Long remove = stringRedisTemplate.opsForSet().remove("like:cache:1", "100");
        System.out.println(remove);
    }
}
