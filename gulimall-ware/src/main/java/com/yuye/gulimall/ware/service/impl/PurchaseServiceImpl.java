package com.yuye.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.ware.dao.PurchaseDao;
import com.yuye.gulimall.ware.entity.PurchaseDetailEntity;
import com.yuye.gulimall.ware.entity.PurchaseEntity;
import com.yuye.gulimall.ware.service.PurchaseDetailService;
import com.yuye.gulimall.ware.service.PurchaseService;
import com.yuye.gulimall.ware.vo.MergeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils unreceiveList(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> purchaseEntityLambdaQueryWrapper = new LambdaQueryWrapper<PurchaseEntity>().eq(PurchaseEntity::getStatus,0);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                purchaseEntityLambdaQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void merge(MergeVO mergeVO) {
        Long purchaseId = mergeVO.getPurchaseId();
        Long[] items = mergeVO.getItems();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        if(purchaseId != null ){
            purchaseEntity = baseMapper.selectById(purchaseId);
        }

        for (Long item : items) {
            PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(item);
            Long wareId = purchaseDetailEntity.getWareId();
            Long purchaseEntityWareId = purchaseEntity.getWareId();
            if(purchaseEntityWareId == null){
                purchaseEntity.setWareId(wareId);
                baseMapper.updateById(purchaseEntity);
                purchaseDetailEntity.setPurchaseId(purchaseEntity.getId());
                purchaseDetailService.updateById(purchaseDetailEntity);
            }else {
                if (wareId == purchaseEntityWareId) {
                    purchaseDetailEntity.setPurchaseId(purchaseEntity.getId());
                    purchaseDetailService.updateById(purchaseDetailEntity);
                }
            }
        }
    }

}