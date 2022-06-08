package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yuye.gulimall.product.dao.AttrDao;
import com.yuye.gulimall.product.dao.AttrGroupDao;
import com.yuye.gulimall.product.dao.CategoryDao;
import com.yuye.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.entity.AttrGroupEntity;
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.AttrService;
import com.yuye.gulimall.product.vo.AttrBaseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private CategoryDao categoryDao;

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
        if (catelogId != 0) {
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            String catelogName = categoryEntity.getName();
            String key = (String) params.get("key");
            String sidx = (String) params.get("sidx");
            String order = (String) params.get("order");
            LambdaQueryWrapper<AttrEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isEmpty(key)) {
                lambdaQueryWrapper = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getCatelogId, catelogId).and(query -> {
                    query.like(AttrEntity::getAttrName, key).or().like(AttrEntity::getAttrType, key);
                });
            } else {
                lambdaQueryWrapper = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getCatelogId, catelogId);
            }
            IPage<AttrEntity> page = this.page(
                    new Query<AttrEntity>().getPage(params),
                    lambdaQueryWrapper
            );
            List<AttrEntity> attrEntities = page.getRecords();
            ArrayList<AttrBaseVO> attrBaseVOS = new ArrayList<>();
            attrEntities.forEach(
                    item -> {
                        Long attrId = item.getAttrId();
                        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
                        Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
                        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                        String attrGroupName = attrGroupEntity.getAttrGroupName();
                        AttrBaseVO attrBaseVO = new AttrBaseVO();
                        attrBaseVO.setAttrId(attrId);
                        attrBaseVO.setAttrName(item.getAttrName());
                        attrBaseVO.setAttrType(item.getAttrType());
                        attrBaseVO.setEnable(item.getEnable());
                        attrBaseVO.setGroupName(attrGroupName);
                        attrBaseVO.setCatelogName(catelogName);
                        attrBaseVO.setSearchType(item.getSearchType());
                        attrBaseVO.setValueType(item.getValueType());
                        attrBaseVO.setIcon(item.getIcon());
                        attrBaseVO.setShowDesc(item.getShowDesc());
                        attrBaseVO.setValueSelect(item.getValueSelect());
                        attrBaseVOS.add(attrBaseVO);
                    }
            );
            IPage<AttrBaseVO> attrBaseVOIPage = new Page<AttrBaseVO>();
            attrBaseVOIPage.setRecords(attrBaseVOS);
            attrBaseVOIPage.setPages(page.getPages());
            attrBaseVOIPage.setCurrent(page.getCurrent());
            attrBaseVOIPage.setSize(page.getSize());
            attrBaseVOIPage.setTotal(page.getTotal());
            return new PageUtils(attrBaseVOIPage);
        }else {
            IPage<AttrEntity> page = this.page(
                    new Query<AttrEntity>().getPage(params),
                    new LambdaQueryWrapper<AttrEntity>()
            );
            List<AttrEntity> attrEntities = page.getRecords();
            ArrayList<AttrBaseVO> attrBaseVOS = new ArrayList<>();
            attrEntities.forEach(
                    item -> {
                        Long attrId = item.getAttrId();
                        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
                        Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
                        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                        String attrGroupName = attrGroupEntity.getAttrGroupName();
                        Long catelogId1 = item.getCatelogId();
                        CategoryEntity categoryEntity = categoryDao.selectById(catelogId1);
                        String catelogName = categoryEntity.getName();
                        AttrBaseVO attrBaseVO = new AttrBaseVO();
                        attrBaseVO.setAttrId(attrId);
                        attrBaseVO.setAttrName(item.getAttrName());
                        attrBaseVO.setAttrType(item.getAttrType());
                        attrBaseVO.setEnable(item.getEnable());
                        attrBaseVO.setGroupName(attrGroupName);
                        attrBaseVO.setCatelogName(catelogName);
                        attrBaseVO.setSearchType(item.getSearchType());
                        attrBaseVO.setValueType(item.getValueType());
                        attrBaseVO.setIcon(item.getIcon());
                        attrBaseVO.setShowDesc(item.getShowDesc());
                        attrBaseVO.setValueSelect(item.getValueSelect());
                        attrBaseVOS.add(attrBaseVO);
                    }
            );
            IPage<AttrBaseVO> attrBaseVOIPage = new Page<AttrBaseVO>();
            attrBaseVOIPage.setRecords(attrBaseVOS);
            attrBaseVOIPage.setPages(page.getPages());
            attrBaseVOIPage.setCurrent(page.getCurrent());
            attrBaseVOIPage.setSize(page.getSize());
            attrBaseVOIPage.setTotal(page.getTotal());
            return new PageUtils(attrBaseVOIPage);
        }
    }
}