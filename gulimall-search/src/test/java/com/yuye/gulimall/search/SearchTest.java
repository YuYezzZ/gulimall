package com.yuye.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.yuye.gulimall.search.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Auther: yuye
 * @Date: 2022/6/16 - 06 - 16 - 20:27
 * @Description: com.yuye.gulimall.search
 * @version: 1.0
 */
@SpringBootTest()
@RunWith(SpringRunner.class)
@Slf4j
public class SearchTest {
    @Value("${es.address}")
    private String address;

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void split() {
        String s = "500_";
        String[] s1 = s.split("_");
        log.info("长度：{}",s1.length);
    }

    @Test
    public void testConn() {
        log.info("全文检索地址是{}", address);
        try {
            EsDataSource esDataSource = new EsDataSource();
            esDataSource.setUrl(address);
            Connection connection = esDataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from newbank where age=35   LIMIT 2"
            );
            while (resultSet.next()) {
                String account_number = resultSet.getString("account_number");
                log.info(account_number);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClient() {
        System.out.println(client);
    }

    @Test
    public void testIndex() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        Account account = new Account();
        account.setAge(30);
        String accountJson = JSON.toJSONString(account);
        indexRequest.source(accountJson, XContentType.JSON);
        indexRequest.id("1");
        IndexResponse index = client.index(indexRequest, RequestOptions.DEFAULT);
        log.info("返回数据:", index);
    }

    @Test
    public void testSearch() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("newbank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address", "mill");
        searchSourceBuilder.query(matchQueryBuilder);
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        log.info("结果:{}", response);
    }
    @Test
    public void testSearchAgg() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("newbank");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("address", "mill");
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10).order(BucketOrder.key(true));
        searchSourceBuilder.query(matchQueryBuilder).aggregation(ageAgg);
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        searchSourceBuilder.aggregation(balanceAvg);
        request.source(searchSourceBuilder);
        log.info("检索条件：{}",searchSourceBuilder);
        SearchResponse response = client.search(request,RequestOptions.DEFAULT);
        log.info("结果:{}", response);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
//            log.info("数据结果：{}",hit);
            String sourceAsString = hit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            log.info("java对象：{}",account);
        }
        Aggregations aggregations = response.getAggregations();
        for (Aggregation aggregation : aggregations) {
            log.info("聚合信息：{}",aggregation.getName());
        }
    }
}