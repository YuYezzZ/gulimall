package com.yuye.gulimall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 16:00
 * @Description: com.yuye.gulimall.order
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("com.yuye.gulimall.order.dao")
@EnableDiscoveryClient
public class GulimallOrderApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class,args);
    }
}
