package com.yuye.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/6/16 - 06 - 16 - 20:04
 * @Description: com.yuye.gulimall.search
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallSearchApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class,args);
    }
}
