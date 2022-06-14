package com.yuye.gulimall.common.to;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:22
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class MemberPriceTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
