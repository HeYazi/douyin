package com.hyz.douyin.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HYZ
 * @date 2023/7/25 12:12
 */
//@SpringBootApplication(scanBasePackages = "com.hyz.douyin.common")
@SpringBootApplication
@MapperScan("com.hyz.douyin.user.mapper")
public class UserMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMainApplication.class, args);
    }
}
