package com.hyz.douyin.interaction;

import com.hyz.douyin.common.model.vo.UserVO;
import com.hyz.douyin.common.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author HYZ
 * @date 2023/8/22 16:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DubboTest {
    @DubboReference
    private InnerUserService innerUserService;

    @Test
    public void test() {
        UserVO userVO = innerUserService.getUserVO(1L);
        System.out.println(userVO);
    }
}
