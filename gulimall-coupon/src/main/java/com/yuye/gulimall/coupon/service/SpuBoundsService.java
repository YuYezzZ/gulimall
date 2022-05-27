package com.yuye.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 08:46:57
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

