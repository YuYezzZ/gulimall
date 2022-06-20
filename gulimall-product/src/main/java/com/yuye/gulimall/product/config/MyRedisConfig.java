package com.yuye.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yuye
 * @Date: 2022/6/18 - 06 - 18 - 23:26
 * @Description: com.yuye.gulimall.product.config
 * @version: 1.0
 */
@Configuration
@EnableCaching
public class MyRedisConfig {
    @Bean
    RedissonClient getRedissonClient(){

        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.127.129:6379");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
