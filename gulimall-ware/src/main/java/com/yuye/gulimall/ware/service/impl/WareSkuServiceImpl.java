package com.yuye.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.ware.dao.WareSkuDao;
import com.yuye.gulimall.ware.entity.WareSkuEntity;
import com.yuye.gulimall.ware.service.WareSkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String skuIdStr = (String) params.get("skuId");
        String wareIdStr = (String) params.get("wareId");
        LambdaQueryWrapper<WareSkuEntity> wareSkuEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(wareIdStr) && !"0".equals(wareIdStr)){
            wareSkuEntityLambdaQueryWrapper.and(query->query.eq(WareSkuEntity::getWareId,Long.parseLong(wareIdStr)));
        }
        if(!StringUtils.isEmpty(skuIdStr) && !"0".equals(skuIdStr)){
            wareSkuEntityLambdaQueryWrapper.and(query->query.eq(WareSkuEntity::getSkuId,Long.parseLong(skuIdStr)));
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wareSkuEntityLambdaQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public boolean hasStock(Long skuId) {
        WareSkuEntity wareSkuEntity = baseMapper.selectById(skuId);
        if (wareSkuEntity!=null && wareSkuEntity.getStock()!=null){
            return wareSkuEntity.getStock()>0;
        }
        return false;
    }

}