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

    private int fullCount;

    private double discount;

    private int countStatus;

    private int fullPrice;

    private int reducePrice;

    private int priceStatus;

    private List<MemberPriceVO> memberPrice;

}
