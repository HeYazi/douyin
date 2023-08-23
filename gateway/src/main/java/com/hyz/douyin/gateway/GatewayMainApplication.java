package com.hyz.douyin.gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HYZ
 * @date 2023/7/29 21:54
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RedissonAutoConfiguration.class})
@EnableDiscoveryClient
@EnableDubbo
public class GatewayMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMainApplication.class, args);
    }
}
