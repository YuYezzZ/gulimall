package com.yuye.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> treeList();

    void deleteByIds(List<Long> asList);

    List<CategoryEntity> getLevel1Category();

    Map<String,  List<Catelog2Vo>> getCatelogJson();
}

