package com.yuye.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.constant.WareConstant;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.ware.dao.PurchaseDao;
import com.yuye.gulimall.ware.entity.PurchaseDetailEntity;
import com.yuye.gulimall.ware.entity.PurchaseEntity;
import com.yuye.gulimall.ware.service.PurchaseDetailService;
import com.yuye.gulimall.ware.service.PurchaseService;
import com.yuye.gulimall.ware.vo.DoneVO;
import com.yuye.gulimall.ware.vo.MergeVO;
import com.yuye.gulimall.ware.vo.PurchaseDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
        LambdaQueryWrapper<PurchaseEntity> purchaseEntityLambdaQueryWrapper = new LambdaQueryWrapper<PurchaseEntity>().lt(PurchaseEntity::getStatus, WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
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
        if (purchaseId != null) {
            purchaseEntity = baseMapper.selectById(purchaseId);
        }else {
            purchaseEntity.setPriority(1);
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            baseMapper.insert(purchaseEntity);
        }

        for (Long item : items) {
            PurchaseDetailEntity purchaseDetailEntity = purchaseDetailService.getById(item);
            Long wareId = purchaseDetailEntity.getWareId();
            Long purchaseEntityWareId = purchaseEntity.getWareId();
            if (purchaseEntityWareId == null && purchaseDetailEntity.getStatus() < WareConstant.PurchaseDetailStatusEnum.PURCHASING.getCode()) {
                purchaseEntity.setWareId(wareId);
                purchaseEntity.setUpdateTime(new Date());
                baseMapper.updateById(purchaseEntity);
                purchaseDetailEntity.setPurchaseId(purchaseEntity.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                purchaseDetailService.updateById(purchaseDetailEntity);
            } else {
                if (wareId == purchaseEntityWareId && purchaseDetailEntity.getStatus() < WareConstant.PurchaseDetailStatusEnum.PURCHASING.getCode()) {
                    purchaseDetailEntity.setPurchaseId(purchaseEntity.getId());
                    purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                    purchaseDetailService.updateById(purchaseDetailEntity);
                }
            }
        }
    }

    @Override
    public void received(Long[] purchaseIds) {
        for (Long purchaseId : purchaseIds) {
            PurchaseEntity purchaseEntity = baseMapper.selectById(purchaseId);
            if (purchaseEntity.getStatus()<WareConstant.PurchaseStatusEnum.RECEIVED.getCode()){
                purchaseEntity.setUpdateTime(new Date());
                purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
                baseMapper.updateById(purchaseEntity);
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.PURCHASING.getCode());
                purchaseDetailService.update(purchaseDetailEntity,new LambdaQueryWrapper<PurchaseDetailEntity>().eq(PurchaseDetailEntity::getPurchaseId,purchaseId));
            }
        }
    }

    @Override
    public void done(DoneVO doneVO) {
        Long purchaseId = doneVO.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISHED.getCode());
        baseMapper.updateById(purchaseEntity);
        List<PurchaseDetailVO> items = doneVO.getItems();
        items.parallelStream().forEach(item->{
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getItemId());
            purchaseDetailEntity.setStatus(item.getStatus());
            purchaseDetailService.updateById(purchaseDetailEntity);
                }
        );

    }

}