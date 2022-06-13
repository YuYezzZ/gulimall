package com.yuye.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:23
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class SkusVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<AttrVO> attr;

    private String skuName;

    private String price;

    private String skuTitle;

    private String skuSubtitle;

    private List<ImagesVO> images;

    private List<String> descar;

    private Integer fullCount;

    private double discount;

    private Integer countStatus;

    private Integer fullPrice;

    private Integer reducePrice;

    private Integer priceStatus;

    private List<MemberPriceVO> memberPrice;

}
