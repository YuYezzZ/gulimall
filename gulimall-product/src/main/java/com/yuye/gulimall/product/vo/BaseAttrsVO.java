package com.yuye.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:20
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class BaseAttrsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long attrId;

    private String attrValues;

    private Integer showDesc;

}
