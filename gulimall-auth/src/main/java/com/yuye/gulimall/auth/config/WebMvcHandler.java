package com.yuye.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: yuye
 * @Date: 2022/6/22 - 06 - 22 - 20:38
 * @Description: com.yuye.gulimall.auth.config
 * @version: 1.0
 */
@Configuration
public class WebMvcHandler implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/reg.html").setViewName("reg");
    }

}
