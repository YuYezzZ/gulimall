package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.AttrDao;
import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.service.AttrService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCid(Map<String, Object> params,Long catelogId) {
        String key = (String) params.get("key");
        String  sidx = (String) params.get("sidx");
        String  order = (String) params.get("order");
        new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getAttrId,catelogId).and(query->{
            query.like(AttrEntity::getAttrName,key).or().like(AttrEntity::getAttrType,key).or().like(a)
        })

        return null;
    }

}