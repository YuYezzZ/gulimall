package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.SkuInfoDao;
import com.yuye.gulimall.product.entity.SkuInfoEntity;
import com.yuye.gulimall.product.service.SkuInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String catelogIdStr = (String) params.get("catelogId");
        String brandIdStr = (String) params.get("brandId");
        String key = (String) params.get("key");
        String minStr = (String) params.get("min");
        String maxStr = (String) params.get("max");
        LambdaQueryWrapper<SkuInfoEntity> skuInfoEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(catelogIdStr) && !catelogIdStr.equals("0")){
            Long catelogId = Long.valueOf(catelogIdStr);
            skuInfoEntityLambdaQueryWrapper.eq(SkuInfoEntity::getCatalogId,catelogId);
        }
        if(!StringUtils.isEmpty(brandIdStr) && !brandIdStr.equals("0")){
            Long brandId = Long.valueOf(brandIdStr);
            skuInfoEntityLambdaQueryWrapper.and(query->query.eq(SkuInfoEntity::getBrandId,brandId));
        }
        if(!StringUtils.isEmpty(key)){
            skuInfoEntityLambdaQueryWrapper.and(query->query.like(SkuInfoEntity::getSkuName,key).or().like(SkuInfoEntity::getSkuDesc,key));
        }
        if(!StringUtils.isEmpty(minStr) && !minStr.equals("0")){
            skuInfoEntityLambdaQueryWrapper.and(query->query.gt(SkuInfoEntity::getPrice, Integer.valueOf(minStr)));
        }
        if(!StringUtils.isEmpty(maxStr)&& !maxStr.equals("0")){
            skuInfoEntityLambdaQueryWrapper.and(query->query.le(SkuInfoEntity::getPrice, Integer.valueOf(maxStr)));
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                skuInfoEntityLambdaQueryWrapper
        );
        return new PageUtils(page);
    }

}