package com.yuye.gulimall.common.to;

import lombok.Data;

import java.io.Serializable;

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

    private Integer buyBounds;

    private Integer growBounds;

}