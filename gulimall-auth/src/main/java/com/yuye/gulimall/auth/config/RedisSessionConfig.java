package com.yuye.gulimall.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Auther: yuye
 * @Date: 2022/6/22 - 06 - 22 - 20:59
 * @Description: com.yuye.gulimall.auth.config
 * @version: 1.0
 */
@Configuration
public class RedisSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("GULIMALL");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern(".gulimall.com");
        return serializer;
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }


}
