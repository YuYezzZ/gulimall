package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.convert.AttrEntityConvert;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


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
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );
        List<AttrBaseVO> attrBaseVOS = page.getRecords().stream().map(item -> {
            Long catelogId1 = item.getCatelogId();
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId1);
            Long attrId = item.getAttrId();
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
            String attrGroupName = attrGroupEntity.getAttrGroupName();
            AttrBaseVO attrBaseVO = AttrEntityConvert.INSTANCE.attrEntityDTO2AttrBaseVO(item);
            attrBaseVO.setGroupName(attrGroupName);
            attrBaseVO.setCatelogName(categoryEntity.getName());
            return attrBaseVO;
        }).collect(Collectors.toList());
        IPage<AttrBaseVO> convert = page.convert(new Function<AttrEntity, AttrBaseVO>() {
            @Override
            public AttrBaseVO apply(AttrEntity attrEntity) {

                return new AttrBaseVO();
            }
        });
        convert.setRecords(attrBaseVOS);
        return new PageUtils(convert);
    }
}