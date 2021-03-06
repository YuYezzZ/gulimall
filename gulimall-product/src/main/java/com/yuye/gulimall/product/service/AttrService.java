package com.yuye.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.vo.AttrFormVO;

import java.util.Map;

/**
 * 商品属性
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByBase(Map<String, Object> params,Long catelogId);

    AttrFormVO getAttrFormVOById(Long attrId);

    PageUtils queryPageBySale(Map<String, Object> params, Long catelogId);

}

