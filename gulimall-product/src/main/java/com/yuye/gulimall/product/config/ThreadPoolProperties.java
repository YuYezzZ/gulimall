package com.yuye.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yuye
 * @Date: 2022/6/21 - 06 - 21 - 19:57
 * @Description: com.yuye.gulimall.product.config
 * @version: 1.0
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Data
@Configuration
public class ThreadPoolProperties {
    private Integer core;
    private Integer max;
    private Long ttl;
    private Integer queue;
}
