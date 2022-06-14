package com.yuye.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveList(List<ProductAttrValueEntity> productAttrValueEntities);

    List<ProductAttrValueEntity> getBySpuId(Long spuId);
}

