package com.yuye.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:25
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class ProductVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String spuName;

    private String spuDescription;

    private int catalogId;

    private int brandId;

    private double weight;

    private int publishStatus;

    private List<String> decript;

    private List<String> images;

    private BoundsVO bounds;

    private List<BaseAttrsVO> baseAttrs;

    private List<SkusVO> skus;
}
