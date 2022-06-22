package com.yuye.gulimall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Auther: yuye
 * @Date: 2022/6/21 - 06 - 21 - 22:31
 * @Description: com.yuye.gulimall.auth
 * @version: 1.0
 */
@SpringBootApplication
@EnableFeignClients("com.yuye.gulimall.auth.feign")
@EnableRedisHttpSession
@EnableDiscoveryClient
public class GulimallAuthApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallAuthApplication.class,args);
    }
}
