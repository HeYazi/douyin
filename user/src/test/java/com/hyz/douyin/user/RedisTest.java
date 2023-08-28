package com.hyz.douyin.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hyz.douyin.common.common.ErrorCode;
import com.hyz.douyin.common.constant.UserConstant;
import com.hyz.douyin.common.exception.BusinessException;

import com.hyz.douyin.user.model.entity.LoginUser;
import com.hyz.douyin.user.model.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author HYZ
 * @date 2023/7/25 17:37
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void beanToMap() {
        User user = new User();
        user.setId(0L);
        user.setUsername("hyz");
        user.setPassword("123456");
        user.setName("何鸭子");
        user.setFollowCount(0L);
        user.setFollowerCount(0L);
        user.setBackgroundImage("");
        user.setAvatar("");
        user.setSignature("");
        user.setTotalFavorited(0L);
        user.setWorkCount(0L);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        LoginUser loginUser = BeanUtil.copyProperties(user, LoginUser.class);
        System.out.println(loginUser);
        Map<String, Object> map = BeanUtil.beanToMap(loginUser, new HashMap<>(1),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        for (String s : map.keySet()) {
            System.out.println(s + "：" + map.get(s));
        }
        stringRedisTemplate.opsForHash().putAll(UserConstant.USER_LOGIN_STATE + "1", map);
    }

    @Test
    public void mapToBean() {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user_login:eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODA1MjgzMmVlODU0MDYyODFhZTRkYjJkNWU1YWUxZSIsInN1YiI6IjEiLCJpc3MiOiJ5ZGxjbGFzcyIsImlhdCI6MTY5MDI4NjUzOCwiZXhwIjoxNjkwMjkwMTM4fQ.nMMi5nl0d9R9AYa-bOKv5Xmd3oo3WQxmdUeqM2YGhak");
        System.out.println(entries);
        LoginUser loginUser = BeanUtil.fillBeanWithMap(entries, new LoginUser(), false);
        System.out.println(loginUser);
    }

    @Test
    public void redisTest() {
        String key = UserConstant.USER_LOGIN_STATE + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhN2IzNjY4MzE1YWQ0Y2YyYjEwODVkNzZlM2RhYzhhZiIsInN1YiI6IjEiLCJpc3MiOiJ5ZGxjbGFzcyIsImlhdCI6MTY5MDM5MDIyNCwiZXhwIjoxNjkwMzkzODI0fQ.PRRRHI-olZW2rSQSObrPLP_KNBVVoxLebNiBcSMuxG8";
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            throw new BusinessException(ErrorCode.USER_OPERATION_ERROR, "请不要重复登录");
        }
    }


}
