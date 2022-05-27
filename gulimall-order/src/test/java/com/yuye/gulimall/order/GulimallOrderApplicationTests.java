package com.yuye.gulimall.order;

import com.yuye.gulimall.order.entity.OrderEntity;
import com.yuye.gulimall.order.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: yuye
 * @Date: 2022/5/27 - 05 - 27 - 9:30
 * @Description: com.yuye.gulimall.order
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallOrderApplicationTests {
    @Autowired
    private OrderService orderService;
    @Test
    //测试订单服务插入
    public void insert(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn("111111111111111");
        orderService.save(orderEntity);
        System.out.println("插入成功");
    }
}
