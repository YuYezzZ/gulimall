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


@Service("skuInfoService")
@Slf4j
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    SkuInfoDao dao;
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
    public SkuItemVO getItem(Long skuId) {
        SkuItemVO skuItemVO = new SkuItemVO();

        SkuInfoEntity skuInfoEntity = baseMapper.selectById(skuId);
        skuItemVO.setInfo(skuInfoEntity);

        List<SkuImagesEntity> images = skuImagesService.list(new LambdaQueryWrapper<SkuImagesEntity>().eq(SkuImagesEntity::getSkuId, skuId));
        skuItemVO.setImages(images);

        Long catelogId = skuInfoEntity.getCatelogId();
        Long spuId = skuInfoEntity.getSpuId();
        List<SkuItemVO.SpuItemAttrGroupVO> spuItemAttrGroupVOS  =  dao.getSpuAttrGroup(spuId,catelogId);
        log.info("spu分组结果：{}",spuItemAttrGroupVOS);
        skuItemVO.setAttrs(spuItemAttrGroupVOS);

        List<SkuItemVO.SkuItemSaleAttrVO> skuItemSaleAttrVOS = dao.getSkuItemSaleAttrVO(spuId);
        log.info("sku销售属性：{}",skuItemSaleAttrVOS);
        skuItemVO.setSaleAttr(skuItemSaleAttrVOS);
        return skuItemVO;
    }

}