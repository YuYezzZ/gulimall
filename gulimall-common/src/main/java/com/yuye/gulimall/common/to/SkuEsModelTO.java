package com.yuye.gulimall.common.to;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/17 - 06 - 17 - 15:39
 * @Description: com.yuye.gulimall.common.to
 * @version: 1.0
 */
@Data
public class SkuEsModelTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catelogId;
    private String brandName;
    private String brandImg;
    private String catelogName;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
