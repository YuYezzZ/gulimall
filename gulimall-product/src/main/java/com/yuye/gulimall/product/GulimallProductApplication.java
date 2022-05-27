package com.yuye.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 16:08
 * @Description: com.yuye.gulimall.product
 * @version: 1.0
 */
@SpringBootApplication
@MapperScan("com.yuye.gulimall.product.dao")/*mybatis-plus映射文件位置*/
@EnableDiscoveryClient
public class GulimallProductApplication {
    //程序入口
    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class,args);
    }
}
