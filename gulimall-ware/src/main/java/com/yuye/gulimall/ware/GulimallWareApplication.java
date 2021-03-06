package com.yuye.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 16:09
 * @Description: com.yuye.gulimall.ware
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yuye.gulimall.ware.dao")
public class GulimallWareApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallWareApplication.class,args);
    }
}
