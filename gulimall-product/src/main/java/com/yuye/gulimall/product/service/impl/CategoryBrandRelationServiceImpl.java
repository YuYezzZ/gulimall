package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.BrandDao;
import com.yuye.gulimall.product.dao.CategoryBrandRelationDao;
import com.yuye.gulimall.product.dao.CategoryDao;
import com.yuye.gulimall.product.entity.*;
import com.yuye.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveName(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectOne(new LambdaQueryWrapper<BrandEntity>().eq(BrandEntity::getBrandId, brandId));
        CategoryEntity categoryEntity = categoryDao.selectOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, catelogId));
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        baseMapper.insert(categoryBrandRelation);
    }

    @Override
    public void updateDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        if(StringUtils.isEmpty(categoryBrandRelation.getBrandName())){
            baseMapper.update(categoryBrandRelation,new UpdateWrapper<CategoryBrandRelationEntity>().lambda().eq(CategoryBrandRelationEntity::getCatelogId,categoryBrandRelation.getCatelogId()));
        }
        baseMapper.update(categoryBrandRelation,new UpdateWrapper<CategoryBrandRelationEntity>().lambda().eq(CategoryBrandRelationEntity::getBrandId,categoryBrandRelation.getBrandId()));
    }

    @Override
    public List<CategoryBrandRelationEntity> brandList(Long catId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationDao.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getCatelogId, catId));
        return categoryBrandRelationEntities;
    }

}