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
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.AttrGroupService;
import com.yuye.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private CategoryService categoryService;
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