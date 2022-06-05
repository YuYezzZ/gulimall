package com.yuye.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/6/4 - 06 - 04 - 16:27
 * @Description: com.yuye.gulimall.thirdparty
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ThirdPartyApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(ThirdPartyApplication.class,args);
    }
}
