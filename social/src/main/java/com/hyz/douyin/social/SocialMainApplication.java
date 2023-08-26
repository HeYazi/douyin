package com.hyz.douyin.social;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HYZ
 * @date 2023/8/23 22:51
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.hyz.douyin.social.mapper")
public class SocialMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialMainApplication.class, args);
    }
}
