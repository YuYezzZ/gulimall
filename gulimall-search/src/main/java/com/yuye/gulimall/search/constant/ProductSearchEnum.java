package com.yuye.gulimall.search.constant;

import org.elasticsearch.search.sort.SortOrder;

/**
 * @Auther: yuye
 * @Date: 2022/6/20 - 06 - 20 - 13:41
 * @Description: com.yuye.gulimall.search.constant
 * @version: 1.0
 */

public enum ProductSearchEnum {
    SkuPriceAsc("skuPrice",SortOrder.ASC),
    SkuPriceDesc("skuPrice",SortOrder.DESC),
    SaleCountAsc("saleCount",SortOrder.ASC),
    SaleCountDesc("saleCount",SortOrder.DESC),
    HotScoreAsc("hotScore",SortOrder.ASC),
    HotScoreDesc("hotScore",SortOrder.DESC);
    private String filedName;
    private SortOrder sortOrder;
    ProductSearchEnum(String filedName,SortOrder sortOrder){
        this.filedName=filedName;
        this.sortOrder=sortOrder;
    }

}
