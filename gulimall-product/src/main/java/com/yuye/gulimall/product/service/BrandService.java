package com.yuye.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.common.utils.PageUtils;
import com.yuye.gulimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

