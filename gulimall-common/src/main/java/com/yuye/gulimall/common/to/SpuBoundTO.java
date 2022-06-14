package com.yuye.gulimall.common.to;

import lombok.Data;

import java.io.Serializable;
import java.lang.Long;
import java.math.BigDecimal;

/**
 * @Auther: yuye
 * @Date: 2022/6/13 - 06 - 13 - 17:20
 * @Description: com.yuye.gulimall.common.to
 * @version: 1.0
 */
@Data
public class SpuBoundTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;

    public void setSpuId(Long spuId){
        this.spuId = spuId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public BigDecimal getBuyBounds() {
        return buyBounds;
    }

    public void setBuyBounds(BigDecimal buyBounds) {
        this.buyBounds = buyBounds;
    }

    public BigDecimal getGrowBounds() {
        return growBounds;
    }

    public void setGrowBounds(BigDecimal growBounds) {
        this.growBounds = growBounds;
    }
}