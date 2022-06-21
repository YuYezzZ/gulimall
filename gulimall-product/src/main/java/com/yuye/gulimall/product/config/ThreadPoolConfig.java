package com.yuye.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: yuye
 * @Date: 2022/6/21 - 06 - 21 - 19:47
 * @Description: com.yuye.gulimall.product.config
 * @version: 1.0
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor( ThreadPoolProperties threadPoolProperties) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                threadPoolProperties.getCore(),
                threadPoolProperties.getMax(),
                threadPoolProperties.getTtl(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(threadPoolProperties.getQueue()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
        return threadPoolExecutor;
    }
}
