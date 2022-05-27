package com.yuye.gulimall.member.feign;

import com.yuye.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: yuye
 * @Date: 2022/5/27 - 05 - 27 - 14:58
 * @Description: com.yuye.gulimall.member.feign
 * @version: 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @GetMapping("coupon/coupon/member/list")
    R membercoupons();

}
