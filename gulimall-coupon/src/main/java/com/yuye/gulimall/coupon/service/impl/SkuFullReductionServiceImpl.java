package com.yuye.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.to.MemberPriceTO;
import com.yuye.gulimall.common.to.SkuReductionTO;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.coupon.dao.SkuFullReductionDao;
import com.yuye.gulimall.coupon.entity.MemberPriceEntity;
import com.yuye.gulimall.coupon.entity.SkuFullReductionEntity;
import com.yuye.gulimall.coupon.entity.SkuLadderEntity;
import com.yuye.gulimall.coupon.service.MemberPriceService;
import com.yuye.gulimall.coupon.service.SkuFullReductionService;
import com.yuye.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    private MemberPriceService memberPriceService;
    @Autowired
    private SkuLadderService skuLadderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTO skuReductionTO) {
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTO,skuFullReductionEntity);
        this.save(skuFullReductionEntity);
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTO,skuLadderEntity);
        skuLadderEntity.setAddOther(skuReductionTO.getCountStatus());
        skuLadderService.save(skuLadderEntity);
        List<MemberPriceTO> memberPrice = skuReductionTO.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntities = memberPrice.parallelStream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTO.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setAddOther(1);
            memberPriceEntity.setMemberPrice(item.getPrice());
            return memberPriceEntity;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntities);
    }

}