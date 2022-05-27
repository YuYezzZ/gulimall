package com.yuye.gulimall.ware;

import com.yuye.gulimall.ware.entity.WareInfoEntity;
import com.yuye.gulimall.ware.service.WareInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: yuye
 * @Date: 2022/5/27 - 05 - 27 - 9:41
 * @Description: com.yuye.gulimall.ware
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallWareApplicationTests {
    @Autowired
    private WareInfoService wareInfoService;
    @Test
    //测试仓库服务插入
    public void insert(){
        WareInfoEntity wareInfoEntity = new WareInfoEntity();
        wareInfoEntity.setName("测试仓库");
        wareInfoService.save(wareInfoEntity);
        System.out.println("插入成功");
    }
}
