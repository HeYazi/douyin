package com.hyz.douyin.basic;

import com.hyz.douyin.basic.utils.JwtUtil;
import org.junit.Test;

/**
 * @author HYZ
 * @date 2023/7/25 16:27
 */
public class JwtTest {
    @Test
    public void test() {
        String jwt = JwtUtil.createJWT("1");
        System.out.println(jwt);
    }
}
