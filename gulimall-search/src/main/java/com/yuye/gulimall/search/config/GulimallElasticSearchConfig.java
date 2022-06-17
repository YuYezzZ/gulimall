package com.yuye.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yuye
 * @Date: 2022/6/17 - 06 - 17 - 10:56
 * @Description: com.yuye.gulimall.search.config
 * @version: 1.0
 */
@Configuration
public class GulimallElasticSearchConfig {
    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                        "192.168.127.129", 9200, "http"
                ))
        );
        return  client;
    }

}
