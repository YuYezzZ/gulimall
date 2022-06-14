package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.util.StringUtils;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.convert.AttrEntityConvert;
import com.yuye.gulimall.product.convert.AttrGroupEntityConvert;
import com.yuye.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.yuye.gulimall.product.dao.AttrDao;
import com.yuye.gulimall.product.dao.AttrGroupDao;
import com.yuye.gulimall.product.entity.*;
import com.yuye.gulimall.product.service.AttrGroupService;
import com.yuye.gulimall.product.service.CategoryService;
import com.yuye.gulimall.product.service.ProductAttrValueService;
import com.yuye.gulimall.product.vo.AttrFormVO;
import com.yuye.gulimall.product.vo.WithattrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrDao attrDao;
    @Autowired
    private ProductAttrValueService productAttrValueService;

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

    @Override
    public List<Long>  getParentIds(Long catelogId) {
        CategoryEntity one = categoryService.getOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, catelogId));
        List<Long> longs = new ArrayList<>();
        Long parentCid1 = one.getParentCid();
        CategoryEntity two = categoryService.getOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, parentCid1));
        Long parentCid = two.getParentCid();
        longs.add(parentCid);
        longs.add(parentCid1);
        longs.add(catelogId);
        return longs;
    }

    @Override
    public List<AttrEntity> attrRelation(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
        List<AttrEntity> collect = attrAttrgroupRelationEntities.stream().map(
                item -> {
                    Long attrId = item.getAttrId();
                    AttrEntity attrEntity = attrDao.selectById(attrId);
                    return attrEntity;
                }
        ).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeList(List<AttrAttrgroupRelationEntity> list) {
        list.stream().forEach(
                item->{
                    Long attrGroupId = item.getAttrGroupId();
                    Long attrId = item.getAttrId();
                    attrAttrgroupRelationDao.delete(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId,attrId).eq(AttrAttrgroupRelationEntity::getAttrGroupId,attrGroupId));
                }
        );
    }

    @Override
    public PageUtils noattrRelation(Map<String, Object> params, Long attrGroupId) {

        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()/*.eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId)*/);
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        String page = (String) params.get("page");
        String limit = (String) params.get("limit");
        String key = (String) params.get("key");
        LambdaQueryWrapper<AttrEntity> lambdaQueryWrapper = new LambdaQueryWrapper<AttrEntity>();
        if(org.apache.commons.lang3.StringUtils.isEmpty(key)){
            lambdaQueryWrapper.and(query->query.like(AttrEntity::getAttrName,key).or().like(AttrEntity::getAttrId,key));
        }
        IPage<AttrEntity> attrEntityIPage = new Page<>(Integer.parseInt(page),Integer.parseInt(limit));
        attrEntityIPage = attrDao.selectPage(attrEntityIPage, lambdaQueryWrapper);
        List<AttrEntity> collect = attrEntityIPage.getRecords().stream().filter(item -> !attrIds.contains(item.getAttrId())
        ).collect(Collectors.toList());
        attrEntityIPage.setRecords(collect);
        return new PageUtils(attrEntityIPage);
    }

    @Override
    public void saveList(List<AttrAttrgroupRelationEntity> list) {
        list.stream().forEach(
                item->attrAttrgroupRelationDao.insert(item)
        );
    }

    @Override
    public List<WithattrVO> selectWithattr(Long catelogId) {
        List<AttrGroupEntity> attrGroupEntities = baseMapper.selectList(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));
        List<WithattrVO> withattrVOList = AttrGroupEntityConvert.INSTANCE.AttrGroupList2WithattrVOList(attrGroupEntities);
        List<WithattrVO> collect = withattrVOList.stream().map(item -> {
            Long attrGroupId = item.getAttrGroupId();
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId));
            ArrayList<AttrFormVO> attrFormVOS = new ArrayList<>();
            attrAttrgroupRelationEntities.stream().forEach(attrAttrgroupRelationEntity -> {
                        Long attrId = attrAttrgroupRelationEntity.getAttrId();
                        AttrEntity attrEntity = attrDao.selectOne(new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getAttrId,attrId).and(query->query.eq(AttrEntity::getAttrType,1)));
                        AttrFormVO attrFormVO = AttrEntityConvert.INSTANCE.attrEntityDTO2AttrFormVO(attrEntity);
                        if(attrFormVO != null){
                            attrFormVOS.add(attrFormVO);
                        }
                    }
            );
            item.setAttrs(attrFormVOS);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 查找父节点Id方法
     */
    private void findParentIds(List<CategoryEntity> list, Long id,List<Long> ids) {
        list.stream()
                .filter(categoryEntity -> categoryEntity.getCatId() == id)
                .forEach(item->{
                    ids.add(item.getParentCid());
                    System.out.println(item.getParentCid());
                    findParentIds(list,item.getParentCid(),ids);
                });
    }

}