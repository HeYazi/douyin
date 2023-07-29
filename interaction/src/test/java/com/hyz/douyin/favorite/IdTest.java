package com.hyz.douyin.favorite;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * @author HYZ
 * @date 2023/7/28 10:06
 */
public class IdTest {
    @Test
    public void snowflake() {
        for (int i = 0; i < 100; i++) {
            long id = IdUtil.getSnowflakeNextId();
            System.out.println(id);
        }

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 100; i++) {
                long id = IdUtil.getSnowflakeNextId();
                System.out.println(id);
            }
        });

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 100; i++) {
                long id = IdUtil.getSnowflakeNextId();
                System.out.println(id);
            }
        });
    }
}
