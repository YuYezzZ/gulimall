package com.yuye.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: yuye
 * @Date: 2022/6/13 - 06 - 13 - 17:10
 * @Description: com.yuye.gulimall.product.feign
 * @version: 1.0
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {
}
