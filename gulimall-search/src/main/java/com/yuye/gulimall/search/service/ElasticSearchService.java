package com.yuye.gulimall.search.service;

import com.yuye.gulimall.common.to.SkuEsModelTO;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/17 - 06 - 17 - 21:16
 * @Description: com.yuye.gulimall.search.service
 * @version: 1.0
 */
public interface ElasticSearchService {
    boolean saveProduct(List<SkuEsModelTO> skuEsModelTOS) throws IOException;
}
