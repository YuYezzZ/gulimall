package com.yuye.gulimall.product.dao;

import com.yuye.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
