package com.yuye.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther: yuye
 * @Date: 2022/6/13 - 06 - 13 - 17:10
 * @Description: com.yuye.gulimall.product.feign
 * @version: 1.0
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    @GetMapping("/ware/waresku/hasStock/{skuId}")
    boolean hasStock(@PathVariable("skuId") Long skuId);
}
