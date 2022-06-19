package com.yuye.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yuye.gulimall.common.to.SkuEsModelTO;
import com.yuye.gulimall.search.service.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/17 - 06 - 17 - 21:16
 * @Description: com.yuye.gulimall.search.service.impl
 * @version: 1.0
 */
@Service("elasticSearchService")
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public boolean saveProduct(List<SkuEsModelTO> skuEsModelTOS) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModelTO skuEsModelTO : skuEsModelTOS) {
            String jsonString = JSON.toJSONString(skuEsModelTO);
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index("product").id(skuEsModelTO.getSkuId().toString());
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }
}
