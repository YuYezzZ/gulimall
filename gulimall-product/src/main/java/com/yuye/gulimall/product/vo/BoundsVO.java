package com.yuye.gulimall.product.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:19
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class BoundsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal buyBounds;

    private BigDecimal growBounds;

}
