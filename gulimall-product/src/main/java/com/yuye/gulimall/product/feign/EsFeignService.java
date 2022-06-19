package com.yuye.gulimall.product.feign;

import com.yuye.gulimall.common.to.SkuEsModelTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/13 - 06 - 13 - 17:10
 * @Description: com.yuye.gulimall.product.feign
 * @version: 1.0
 */
@FeignClient("gulimall-search")
public interface EsFeignService {
    @PostMapping("/search/product/save")
    void saveProduct(@RequestBody List<SkuEsModelTO> skuEsModelTOS);
}
