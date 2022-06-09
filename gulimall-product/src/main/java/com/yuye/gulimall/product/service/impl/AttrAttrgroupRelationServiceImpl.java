package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yuye.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yuye.gulimall.product.service.AttrAttrgroupRelationService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updateByAttrId(Long attrId, Long attrGroupId) {
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = baseMapper.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
        if (attrAttrgroupRelationEntity == null){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity1 = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity1.setAttrId(attrId);
            attrAttrgroupRelationEntity1.setAttrGroupId(attrGroupId);
            baseMapper.insert(attrAttrgroupRelationEntity1);
        }else {
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            baseMapper.updateById(attrAttrgroupRelationEntity);
        }

    }

}