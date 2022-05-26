package com.yuye.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuye.gulimall.product.entity.BrandEntity;
import com.yuye.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 19:25
 * @Description: com.yuye.gulimall.product
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Autowired
    private BrandService brandService;
    
    //测试品牌插入功能
    @Test
    public void insert(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandEntity.setDescript("万物互联");
        boolean save = brandService.save(brandEntity);
        System.out.println(save);
    }

    @Test
    public void query(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        LambdaQueryWrapper<BrandEntity> entityLambdaQueryWrapper = new LambdaQueryWrapper<BrandEntity>().eq(BrandEntity::getBrandId, brandEntity.getBrandId());
        BrandEntity one = brandService.getOne(entityLambdaQueryWrapper);
        System.out.println(one);
    }
    
}
