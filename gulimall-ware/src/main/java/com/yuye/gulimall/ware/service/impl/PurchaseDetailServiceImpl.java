package com.yuye.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.ware.dao.PurchaseDetailDao;
import com.yuye.gulimall.ware.entity.PurchaseDetailEntity;
import com.yuye.gulimall.ware.service.PurchaseDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareIdStr = (String) params.get("wareId");
        LambdaQueryWrapper<PurchaseDetailEntity> purchaseDetailEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(wareIdStr) && !"0".equals(wareIdStr)){
            purchaseDetailEntityLambdaQueryWrapper.and(query->query.eq(PurchaseDetailEntity::getWareId,Long.valueOf(wareIdStr)));
        }
        if(!StringUtils.isEmpty(key)){
            purchaseDetailEntityLambdaQueryWrapper.and(query->query.like(PurchaseDetailEntity::getSkuId,key).or().like(PurchaseDetailEntity::getWareId,key));
        }
        if (!StringUtils.isEmpty(status)){
            purchaseDetailEntityLambdaQueryWrapper.and(query->query.eq(PurchaseDetailEntity::getStatus,Integer.valueOf(status)));
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                purchaseDetailEntityLambdaQueryWrapper
        );

        return new PageUtils(page);
    }

}