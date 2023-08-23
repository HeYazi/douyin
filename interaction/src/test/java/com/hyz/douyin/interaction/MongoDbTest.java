package com.hyz.douyin.interaction;

import cn.hutool.core.util.IdUtil;
import com.hyz.douyin.interaction.constant.CollectionConstant;
import com.hyz.douyin.interaction.model.entity.Comment;
import com.hyz.douyin.interaction.model.entity.Favorite;

import java.util.Date;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;


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
        Comment comment = new Comment();
        comment.setId(0L);
        comment.setUserId(1L);
        comment.setVideoId(0L);
        comment.setCommentText("");
        comment.setCreateTime(new Date());
        mongoTemplate.insert(comment);
    }

    @Test
    public void testQuery() {
        Comment comment = mongoTemplate.findOne(
                Query.query(
                        Criteria.where("id")
                                .is(0L)
                                .and("user_id")
                                .is(1L)
                                .and("video_id")
                                .is(0L)
                ), Comment.class);
        System.out.println(comment);
    }

    @Test
    public void testDelete() {
        mongoTemplate.remove(Query.query(
                Criteria.where("id")
                        .is(0L)
                        .and("user_id")
                        .is(0L).
                        and("video_id").
                        is(0L)
        ), Comment.class);
    }
}
