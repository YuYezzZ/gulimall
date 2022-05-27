package com.yuye.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 08:46:57
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

