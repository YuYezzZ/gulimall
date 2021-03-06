package com.yuye.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
    @Bean
    RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        //得到消息转换器
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        //指定StringHttpMessageConverter消息转换器的字符集为utf-8
        messageConverters.set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return  restTemplate;
    }
}
