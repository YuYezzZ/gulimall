package com.yuye.gulimall.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/13 - 06 - 13 - 23:24
 * @Description: com.yuye.gulimall.common.to
 * @version: 1.0
 */
@Data
public class SkuReductionTO {
    private Long skuId;

    private int fullCount;

    private BigDecimal discount;

    private int countStatus;

    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    private int priceStatus;

    private List<MemberPriceTO> memberPrice;
}
