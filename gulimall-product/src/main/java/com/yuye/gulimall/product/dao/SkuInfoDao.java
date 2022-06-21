package com.yuye.gulimall.product.dao;

import com.yuye.gulimall.product.entity.SkuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuye.gulimall.product.vo.SkuItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku信息
 * 
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {
    List<SkuItemVO.SpuItemAttrGroupVO> getSpuAttrGroup(@Param("spuId") Long spuId, @Param("catelogId") Long catelogId);

    List<SkuItemVO.SkuItemSaleAttrVO> getSkuItemSaleAttrVO(Long spuId);

}
