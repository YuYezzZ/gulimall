package com.yuye.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.ware.dao.WareInfoDao;
import com.yuye.gulimall.ware.entity.WareInfoEntity;
import com.yuye.gulimall.ware.service.WareInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String key = (String) params.get("key");
        LambdaQueryWrapper<WareInfoEntity> wareInfoEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            wareInfoEntityLambdaQueryWrapper.like(WareInfoEntity::getAddress,key).or().like(WareInfoEntity::getName,key).or().like(WareInfoEntity::getAreacode,key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wareInfoEntityLambdaQueryWrapper
        );

        return new PageUtils(page);
    }

}