package com.hyz.douyin.user.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HYZ
 * @date 2023/7/28 20:46
 */
public interface UserConstant {
    /**
     * 用户信息状态
     */
    String USER_INFO_STATE = "user:info:";

    /**
     * 用户信息锁
     */
    String USER_INFO_LOCK = "user:info:lock:";

    /**
     * 用户令牌盐
     */
    Map<String, Object> USER_TOKEN_MAP = new HashMap<String, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put("uid", Integer.parseInt("114514"));
            put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
        }
    };

    /**
     * 登录用户ttl
     */
    Long LOGIN_USER_TTL = 30L;

    /**
     * 查询用户ttl
     */
    Long QUERY_USER_TTL = 5L;
}
