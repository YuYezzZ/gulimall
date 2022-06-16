package com.yuye.gulimall.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
public class SearchTest {
    @Value("${es.address}")
    private String address;

    private final static Logger LOGGER = LogManager.getLogger(SearchTest.class);
    @Test
    public void testConn() {
        LOGGER.info(address);
        try {
            EsDataSource esDataSource = new EsDataSource();
            esDataSource.setUrl(address);
            Connection connection = esDataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from newbank where age=35   LIMIT 2"
            );
            while(resultSet.next()){
                String account_number = resultSet.getString("account_number");
                LOGGER.info(account_number);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
