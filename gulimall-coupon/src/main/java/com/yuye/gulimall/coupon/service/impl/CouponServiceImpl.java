package com.yuye.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.coupon.dao.CouponDao;
import com.yuye.gulimall.coupon.entity.CouponEntity;
import com.yuye.gulimall.coupon.service.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponDao, CouponEntity> implements CouponService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String pageNum = (String) params.get("page");
        String limit = (String) params.get("limit");
        String key = (String) params.get("key");
        LambdaQueryWrapper<CouponEntity> lambdaQueryWrapper = new LambdaQueryWrapper<CouponEntity>();

        if(!StringUtils.isEmpty(key)){
            lambdaQueryWrapper.and(query->query.like(CouponEntity::getCouponName,key));
        }
        IPage<CouponEntity> couponEntityIPage = new Page<>(Integer.parseInt(pageNum),Integer.parseInt(limit));
        IPage<CouponEntity> page = baseMapper.selectPage(couponEntityIPage, lambdaQueryWrapper);
        return new PageUtils(page);
    }

}