package com.yuye.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 10:03
 * @Description: com.yuye
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("com.yuye.gulimall.member.dao")
@EnableDiscoveryClient
@EnableFeignClients("com.yuye.gulimall.member.feign")
public class GulimallMemberApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class,args);
    }
}
