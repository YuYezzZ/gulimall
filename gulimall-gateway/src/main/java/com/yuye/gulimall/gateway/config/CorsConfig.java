package com.yuye.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @Auther: yuye
 * @Date: 2022/5/30 - 05 - 30 - 12:11
 * @Description: com.yuye.gulimall.gateway.config
 * @version: 1.0
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter getCorsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //配置跨域
        //允许那些请求头跨域
        corsConfiguration.addAllowedHeader("*");
        //允许那些请求方法跨域
        corsConfiguration.addAllowedMethod("*");
        //允许那些请求来源跨域
        corsConfiguration.addAllowedOrigin("*");
        //是否允许携带cookie跨域
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}
