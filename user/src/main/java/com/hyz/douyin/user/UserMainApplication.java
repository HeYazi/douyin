package com.hyz.douyin.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HYZ
 * @date 2023/7/25 12:12
 */
@SpringBootApplication
@MapperScan("com.hyz.douyin.user.mapper")
@EnableDubbo
public class UserMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMainApplication.class, args);
    }
}
