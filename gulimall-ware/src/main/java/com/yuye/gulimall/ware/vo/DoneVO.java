package com.yuye.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/15 - 06 - 15 - 9:31
 * @Description: com.yuye.gulimall.ware.vo
 * @version: 1.0
 */
@Data
public class DoneVO {
    private Long id;
    private List<PurchaseDetailVO> items;
}
