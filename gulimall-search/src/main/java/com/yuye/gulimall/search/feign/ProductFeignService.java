package com.yuye.gulimall.search.feign;

import com.yuye.gulimall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther: yuye
 * @Date: 2022/6/20 - 06 - 20 - 21:08
 * @Description: com.yuye.gulimall.search.feign
 * @version: 1.0
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @GetMapping("/product/attr/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
     R attrInfo(@PathVariable("attrId") Long attrId);
}
