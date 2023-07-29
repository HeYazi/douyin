package com.hyz.douyin.user;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HYZ
 * @date 2023/7/25 16:27
 */
public class JwtTest {

    @Test
    public void createJWT() {
        Map<String, Object> map = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
                put("uid", Integer.parseInt("123"));
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
            }
        };

        String token1 = JWTUtil.createToken(map, "1234".getBytes());
        System.out.println(token1);
    }

    @Test
    public void analyzeJWT() {
        String rightToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJ1aWQiOjEyMywiZXhwaXJlX3RpbWUiOjE2OTE3MjY3NzQ2MzN9." +
                "u4gM58BtlHbc9Cnv2mfctQmxusy8lj-SdUS_gd7uHEk";

        final JWT jwt = JWTUtil.parseToken(rightToken);

        jwt.getHeader(JWTHeader.TYPE);
        jwt.getPayload("sub");
    }

    @Test
    public void validation() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
                "eyJ1aWQiOjEyMywiZXhwaXJlX3RpbWUiOjE2OTE3MjY3NzQ2MzN9." +
                "u4gM58BtlHbc9Cnv2mfctQmxusy8lj-SdUS_gd7uHEk";

        boolean verify = JWTUtil.verify(token, "123".getBytes());
        System.out.println(verify);
    }
}
