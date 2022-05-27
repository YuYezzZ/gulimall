package com.yuye.gulimall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/5/27 - 05 - 27 - 16:45
 * @Description: com.yuye.gulimall.gateway
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallGatewayApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallGatewayApplication.class,args);
    }
}
