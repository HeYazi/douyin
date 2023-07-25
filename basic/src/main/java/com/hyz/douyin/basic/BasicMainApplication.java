package com.hyz.douyin.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HYZ
 * @date 2023/7/25 12:12
 */
@SpringBootApplication
@MapperScan("com.hyz.douyin.basic.mapper")
public class BasicMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicMainApplication.class, args);
    }
}
