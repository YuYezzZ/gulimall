package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.util.StringUtils;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.AttrGroupDao;
import com.yuye.gulimall.product.entity.AttrGroupEntity;
import com.yuye.gulimall.product.service.AttrGroupService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageId(Map<String, Object> params, Long catId) {

        if(catId!=0){
            LambdaQueryWrapper<AttrGroupEntity> queryWrapper = new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catId);
            String key = (String) params.get("key");
            if(!StringUtils.isNullOrEmpty(key)){
                queryWrapper.and(query->{
                    query.eq(AttrGroupEntity::getAttrGroupName,key).or().like(AttrGroupEntity::getDescript,key);
                });
            }
            IPage<AttrGroupEntity> iPage = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper
            );
            return new PageUtils(iPage);
        }

        return queryPage(params);
    }

}