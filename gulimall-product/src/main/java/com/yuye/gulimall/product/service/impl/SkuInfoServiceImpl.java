package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.SkuInfoDao;
import com.yuye.gulimall.product.entity.SkuImagesEntity;
import com.yuye.gulimall.product.entity.SkuInfoEntity;
import com.yuye.gulimall.product.service.AttrGroupService;
import com.yuye.gulimall.product.service.SkuImagesService;
import com.yuye.gulimall.product.service.SkuInfoService;
import com.yuye.gulimall.product.vo.SkuItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service("skuInfoService")
@Slf4j
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    SkuInfoDao dao;
    @Autowired
    ThreadPoolExecutor executor;
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
            skuInfoEntityLambdaQueryWrapper.eq(SkuInfoEntity::getCatelogId,catelogId);
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

    @Override
    public SkuItemVO getItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVO skuItemVO = new SkuItemVO();
       ;
        CompletableFuture<SkuInfoEntity> skuInfoFuture = new CompletableFuture<SkuInfoEntity>().supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = baseMapper.selectById(skuId);
            skuItemVO.setInfo(skuInfoEntity);
            log.info("当前线程id：{}",Thread.currentThread().getId());
            return skuInfoEntity;
        },executor);

        CompletableFuture<Void> spuItemAttrGroupFuture = skuInfoFuture.thenAcceptAsync((item) -> {
            Long catelogId = item.getCatelogId();
            Long spuId = item.getSpuId();
            List<SkuItemVO.SpuItemAttrGroupVO> spuItemAttrGroupVOS = dao.getSpuAttrGroup(spuId, catelogId);
            log.info("spu分组结果：{}", spuItemAttrGroupVOS);
            skuItemVO.setAttrs(spuItemAttrGroupVOS);
            log.info("当前线程id：{}",Thread.currentThread().getId());
        },executor);

        CompletableFuture<Void> skuItemSaleAttrFuture = skuInfoFuture.thenAcceptAsync((item) -> {
            Long spuId = item.getSpuId();
            List<SkuItemVO.SkuItemSaleAttrVO> skuItemSaleAttrVOS = dao.getSkuItemSaleAttrVO(spuId);
            log.info("sku销售属性：{}",skuItemSaleAttrVOS);
            skuItemVO.setSaleAttr(skuItemSaleAttrVOS);
            log.info("当前线程id：{}",Thread.currentThread().getId());
        },executor);

        CompletableFuture<Void> imgFuture = new CompletableFuture<SkuImagesEntity>().runAsync(() -> {
            List<SkuImagesEntity> images = skuImagesService.list(new LambdaQueryWrapper<SkuImagesEntity>().eq(SkuImagesEntity::getSkuId, skuId));
            skuItemVO.setImages(images);
            log.info("当前线程id：{}",Thread.currentThread().getId());
        }, executor);


        CompletableFuture.allOf(skuInfoFuture,spuItemAttrGroupFuture,skuItemSaleAttrFuture,imgFuture).get();

        log.info("skuItemVO属性==================================={}",skuItemVO);
        return skuItemVO;
    }

}