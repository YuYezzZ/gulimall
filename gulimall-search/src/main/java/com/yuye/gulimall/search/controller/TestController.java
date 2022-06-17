package com.yuye.gulimall.search.controller;


import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Auther: yuye
 * @Date: 2022/6/16 - 06 - 16 - 21:42
 * @Description: com.yuye.gulimall.search.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Value("${es.address}")
    private String address;
    @GetMapping("/test")
    public ResultSet testConn() throws SQLException {

        EsDataSource esDataSource = new EsDataSource();
        esDataSource.setUrl(address);
        Connection connection = esDataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from newbank where age=35");
        return  resultSet;
    }
}
