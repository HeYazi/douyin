package com.hyz.douyin.interaction;

import cn.hutool.json.JSONUtil;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.interaction.model.entity.Comment;
import com.hyz.douyin.interaction.model.entity.Favorite;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
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

    @Test
    public void testGetUserId() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExNDUxNCwiZXhwaXJlX3RpbWUiOjE2OTI3NjE3MTUyMzd9.EVmxs7zxi9OkKEIRA5lccpeM_U9ECjAUcK1qxcduU2E";
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.USER_LOGIN_STATE + token);
        System.out.println(Long.valueOf((String) entries.get("id")));
    }

    @Test
    public void commentTest() {
        Comment comment = new Comment();
        comment.setId(0L);
        comment.setUserId(0L);
        comment.setVideoId(0L);
        comment.setCommentText("");
        comment.setCreateTime(new Date());
        String s = JSONUtil.toJsonStr(comment);
        stringRedisTemplate.opsForValue().set("aaa", s);
    }

    @Test
    public void mongoTest() {
        String s = stringRedisTemplate.opsForValue().get("aaa");
        Comment comment = JSONUtil.toBean(s, Comment.class);
        System.out.println(comment);
    }

    @Test
    public void strTest() {
        String a = "123:456:789";
        String[] split = a.split(":");
        for (String s : split) {
            System.out.println(s);
        }
    }
}
