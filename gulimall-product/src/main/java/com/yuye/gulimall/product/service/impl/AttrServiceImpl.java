package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.convert.AttrEntityConvert;
import com.yuye.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yuye.gulimall.product.dao.AttrDao;
import com.yuye.gulimall.product.dao.AttrGroupDao;
import com.yuye.gulimall.product.dao.CategoryDao;
import com.yuye.gulimall.product.entity.*;
import com.yuye.gulimall.product.service.AttrGroupService;
import com.yuye.gulimall.product.service.AttrService;
import com.yuye.gulimall.product.vo.AttrBaseVO;
import com.yuye.gulimall.product.vo.AttrFormVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByBase(Map<String, Object> params,Long catelogId) {
        String page1 = (String) params.get("page");
        String limit = (String) params.get("limit");
        String key = (String) params.get("key");
        String order = (String) params.get("order");
        String sidx = (String) params.get("sidx");
        LambdaQueryWrapper<AttrEntity> lambdaQueryWrapper = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getAttrType,1);
        if(catelogId != 0){
            lambdaQueryWrapper.and(query->query.eq(AttrEntity::getCatelogId,catelogId));
        }
        if(!StringUtils.isEmpty(key)){
            lambdaQueryWrapper.and(query->query.like(AttrEntity::getAttrName,key).or().like(AttrEntity::getAttrId,key));
        }
//        if (!StringUtils.isEmpty(order) && order.equals("asc")){
//            if(!StringUtils.isEmpty(sidx)){
//                switch (sidx) {
//                    case "attrId" : lambdaQueryWrapper.orderByAsc(AttrEntity::getAttrId);
//                    case "attrId" : lambdaQueryWrapper.orderByAsc(AttrEntity::getAttrId);
//                    case "attrId" : lambdaQueryWrapper.orderByAsc(AttrEntity::getAttrId);
//                    case "attrId" : lambdaQueryWrapper.orderByAsc(AttrEntity::getAttrId);
//                    case "attrId" : lambdaQueryWrapper.orderByAsc(AttrEntity::getAttrId);
//                    case "attrId" : lambdaQueryWrapper.orderByAsc(AttrEntity::getAttrId);
//                }
//            }
//        }

        IPage<AttrEntity> attrEntityIPage = new Page<>(Integer.parseInt(page1),Integer.parseInt(limit));
        IPage<AttrEntity> page = baseMapper.selectPage(attrEntityIPage, lambdaQueryWrapper);
//        IPage<AttrEntity> page = this.page(
//                new Query<AttrEntity>().getPage(params),
//                new QueryWrapper<AttrEntity>()
//        );
        IPage<AttrBaseVO> convert = getAttrBaseVOIPage(page);
        return new PageUtils(convert);
    }

    private IPage<AttrBaseVO> getAttrBaseVOIPage(IPage<AttrEntity> page) {
        List<AttrBaseVO> attrBaseVOS = page.getRecords().stream().map(item -> {
            Long catelogId1 = item.getCatelogId();
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId1);
            Long attrId = item.getAttrId();
            String attrGroupName = "无";
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
            if (attrAttrgroupRelationEntity != null) {
                Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                attrGroupName = attrGroupEntity.getAttrGroupName();
            }
            AttrBaseVO attrBaseVO = AttrEntityConvert.INSTANCE.attrEntityDTO2AttrBaseVO(item);
            attrBaseVO.setGroupName(attrGroupName);
            attrBaseVO.setCatelogName(categoryEntity.getName());
            return attrBaseVO;
        }).collect(Collectors.toList());
        IPage<AttrBaseVO> convert = page.convert(attrEntity -> new AttrBaseVO());
        convert.setRecords(attrBaseVOS);
        convert.setTotal(page.getTotal());
        convert.setSize(page.getSize());
        convert.setCurrent(page.getCurrent());
        convert.setPages(page.getPages());
        return convert;
    }

    @Override
    public AttrFormVO getAttrFormVOById(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        Long catelogId = attrEntity.getCatelogId();
        List<Long> parentIds = attrGroupService.getParentIds(catelogId);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId,attrId));
        Long attrGroupId = 0L;
        if(attrAttrgroupRelationEntity !=null){
            attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
        }
        AttrFormVO attrFormVO = AttrEntityConvert.INSTANCE.attrEntityDTO2AttrFormVO(attrEntity);
        attrFormVO.setAttrGroupId(attrGroupId);
        attrFormVO.setCatelogPath(parentIds);
        return attrFormVO;
    }

    @Override
    public PageUtils queryPageBySale(Map<String, Object> params, Long catelogId) {
        String page1 = (String) params.get("page");
        String limit = (String) params.get("limit");
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrEntity> lambdaQueryWrapper = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getAttrType,0);
        if(catelogId != 0){
            lambdaQueryWrapper.and(query->query.eq(AttrEntity::getCatelogId,catelogId));
        }
        if(!StringUtils.isEmpty(key)){
            lambdaQueryWrapper.and(query->query.like(AttrEntity::getAttrName,key).or().like(AttrEntity::getAttrId,key));
        }
        IPage<AttrEntity> attrEntityIPage = new Page<>(Integer.parseInt(page1),Integer.parseInt(limit));
        IPage<AttrEntity> page = baseMapper.selectPage(attrEntityIPage, lambdaQueryWrapper);
        IPage<AttrBaseVO> convert = getAttrBaseVOIPage(page);
        return new PageUtils(convert);
    }
}