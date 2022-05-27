package com.yuye.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 9:56
 * @Description: com.yuye
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("com.yuye.gulimall.coupon.dao")
@EnableDiscoveryClient
public class GulimallCouponApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallCouponApplication.class,args);
    }
}
