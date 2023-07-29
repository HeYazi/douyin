package com.hyz.douyin.favorite;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyz.douyin.favorite.model.entity.Favorite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author HYZ
 * @date 2023/7/28 14:28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDbTest {
    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testAdd() {
        Favorite favorite = new Favorite();
        favorite.setId(IdUtil.getSnowflakeNextId());
        favorite.setUserId(1L);
        favorite.setVideoId(1L);
        mongoTemplate.save(favorite);
    }

    @Test
    public void testQuery() {
        Favorite favorite = mongoTemplate.findOne(
                Query.query(
                        Criteria.where("user_id")
                                .is(1L)
                                .and("video_id")
                                .is(1L)
                ), Favorite.class);
    }

    @Test
    public void testDelete() {
        mongoTemplate.remove(Query.query(Criteria.where("user_id").is(1L).and("video_id").in()));
    }
}
