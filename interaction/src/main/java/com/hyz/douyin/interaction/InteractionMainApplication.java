package com.hyz.douyin.interaction;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HYZ
 * @date 2023/7/27 18:14
 */
@SpringBootApplication
@EnableDubbo
public class InteractionMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(InteractionMainApplication.class, args);
    }
}
