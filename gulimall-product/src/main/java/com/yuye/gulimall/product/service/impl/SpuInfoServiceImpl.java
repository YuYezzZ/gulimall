package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.to.SkuReductionTO;
import com.yuye.gulimall.common.to.SpuBoundTO;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.SpuInfoDao;
import com.yuye.gulimall.product.entity.*;
import com.yuye.gulimall.product.feign.CouponFeignService;
import com.yuye.gulimall.product.service.*;
import com.yuye.gulimall.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String catelogIdStr = (String) params.get("catelogId");
        String brandIdStr = (String) params.get("brandId");
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        LambdaQueryWrapper<SpuInfoEntity> spuInfoEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(catelogIdStr) && !catelogIdStr.equals("0")){
            Long catelogId = Long.valueOf(catelogIdStr);
            spuInfoEntityLambdaQueryWrapper.eq(SpuInfoEntity::getCatalogId,catelogId);
        }
        if(!StringUtils.isEmpty(brandIdStr) && !brandIdStr.equals("0")){
            Long brandId = Long.valueOf(brandIdStr);
            spuInfoEntityLambdaQueryWrapper.and(query->query.eq(SpuInfoEntity::getBrandId,brandId));
        }
        if(!StringUtils.isEmpty(key)){
            spuInfoEntityLambdaQueryWrapper.and(query->query.like(SpuInfoEntity::getSpuName,key).or().like(SpuInfoEntity::getSpuDescription,key));
        }
        if(!StringUtils.isEmpty(status)){
            spuInfoEntityLambdaQueryWrapper.and(query->query.eq(SpuInfoEntity::getPublishStatus, Integer.valueOf(status)));
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                spuInfoEntityLambdaQueryWrapper
        );
        IPage<SpuInfoVO> spuInfoVOPage = new Page<SpuInfoVO>();
        spuInfoVOPage.setPages(page.getPages());
        spuInfoVOPage.setCurrent(page.getCurrent());
        spuInfoVOPage.setTotal(page.getTotal());
        spuInfoVOPage.setSize(page.getSize());

        List<SpuInfoVO> spuInfoVOS = page.getRecords().parallelStream().map(item -> {
            SpuInfoVO spuInfoVO = new SpuInfoVO();
            BeanUtils.copyProperties(item, spuInfoVO);
            Long itemBrandId = item.getBrandId();
            BrandEntity brandEntity = brandService.getById(itemBrandId);
            spuInfoVO.setBrandName(brandEntity.getName());
            return spuInfoVO;
        }).collect(Collectors.toList());
        spuInfoVOPage.setRecords(spuInfoVOS);
        return new PageUtils(spuInfoVOPage);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVO spuSaveVO) {
        //1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVO,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
        //2.保存spu的描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.save(spuInfoDescEntity);
        //3.保存spu的图片集 pms_spu_images
        List<String> images = spuSaveVO.getImages();
        spuImagesService.saveImages(spuId,images);
        //4.保存spu的规格参数 pms_product_attr_value
        List<BaseAttrsVO> baseAttrs = spuSaveVO.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(e -> {
            AttrEntity attrEntity = attrService.getById(e.getAttrId());
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            BeanUtils.copyProperties(e, productAttrValueEntity);
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setSpuId(spuId);
            productAttrValueEntity.setAttrValue(e.getAttrValues());
            productAttrValueEntity.setQuickShow(e.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveList(productAttrValueEntities);

        //5.保存spu的积分信息 gulimall-sms -> sms_spu_bounds
        BoundsVO bounds = spuSaveVO.getBounds();
        SpuBoundTO spuBoundTO = new SpuBoundTO();
        BeanUtils.copyProperties(bounds,spuBoundTO);
        spuBoundTO.setSpuId(spuId);
        couponFeignService.saveSpubounds(spuBoundTO);
        //6.保存当前spu对应的所有sku信息
        //6.1) sku的基本信息 pms_sku_info
        List<SkusVO> skus = spuSaveVO.getSkus();
        if(skus !=null && skus.size()>0){
            skus.parallelStream().forEach(sku->{

                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setSkuDefaultImg(null);
                List<ImagesVO> skuImages = sku.getImages();
                if(skuImages != null && skuImages.size()>0) {
                    List<SkuImagesEntity> skuImagesEntities = skuImages.parallelStream().map(skuImage -> {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        BeanUtils.copyProperties(skuImage,skuImagesEntity);
                        Integer defaultImg = skuImage.getDefaultImg();
                        if (defaultImg == 1) {
                            skuInfoEntity.setSkuDefaultImg(skuImage.getImgUrl());
                        }
                        skuInfoService.save(skuInfoEntity);
                        Long skuId = skuInfoEntity.getSkuId();
                        skuImagesEntity.setSkuId(skuId);
                        return skuImagesEntity;
                    }).collect(Collectors.toList());
                    //6.2)sku的图片信息 pms_sku_images
                    skuImagesService.saveList(skuImagesEntities);
                }else{
                    skuInfoService.save(skuInfoEntity);
                }
                //6.3)sku的销售属性信息
                List<AttrVO> attr = sku.getAttr();
                attr.parallelStream().forEach(attrVO -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attrVO,skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuSaleAttrValueService.save(skuSaleAttrValueEntity);
                });
                //6.4)会员价
                SkuReductionTO skuReductionTO = new SkuReductionTO();
                BeanUtils.copyProperties(sku,skuReductionTO);
                skuReductionTO.setSkuId(skuInfoEntity.getSkuId());
                List<MemberPriceVO> memberPrice = sku.getMemberPrice();
                couponFeignService.saveSkuReduction(skuReductionTO);
            });


        }

    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        baseMapper.insert(spuInfoEntity);
    }

}