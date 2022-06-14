package com.yuye.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:25
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class SpuSaveVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String spuName;

    private String spuDescription;

    private Long catalogId;

    private Long brandId;

    private BigDecimal weight;

    private int publishStatus;

    private List<String> decript;

    private List<String> images;

    private BoundsVO bounds;

    private List<BaseAttrsVO> baseAttrs;

    private List<SkusVO> skus;
}
