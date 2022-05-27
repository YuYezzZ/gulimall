package com.yuye.gulimall.coupon;

import com.yuye.gulimall.coupon.entity.CouponEntity;
import com.yuye.gulimall.coupon.service.CouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: yuye
 * @Date: 2022/5/27 - 05 - 27 - 8:37
 * @Description: com.yuye.gulimall.coupon
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallCouponApplicationTests {
    @Autowired
    private CouponService couponService;

    //优惠服务插入测试
    @Test
    public void insert(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("限时优惠");
        couponService.save(couponEntity);
        System.out.println("插入成功");
    }
}
