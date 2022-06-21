package com.yuye.gulimall.product.vo;

import com.yuye.gulimall.product.entity.SkuImagesEntity;
import com.yuye.gulimall.product.entity.SkuInfoEntity;
import com.yuye.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/21 - 06 - 21 - 8:50
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class SkuItemVO {
    private SkuInfoEntity info;
    private List<SkuImagesEntity> images;
    private List<SkuItemSaleAttrVO> saleAttr;
    private SpuInfoDescEntity desp;
    private List<SpuItemAttrGroupVO> attrs;

    @Data
    public static class SkuItemSaleAttrVO {
        private Long attrId;
        private String attrName;
        private List<SkuItemSaleAttrWithSkuIdVO> skuItemSaleAttrWithSkuIdVOS;
    }

    @Data
    public static class SpuItemAttrGroupVO {
        private String groupName;
        private List<SpuBaseAttrVO> attrs;
    }
    @Data
    public static class SpuBaseAttrVO{
        private String attrName;
        private String attrValues;
    }
    @Data
    public static class SkuItemSaleAttrWithSkuIdVO {
        private String attrValue;
        private String skuIds;
    }
}
