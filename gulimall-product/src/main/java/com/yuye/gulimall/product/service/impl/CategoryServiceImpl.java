package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.CategoryDao;
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> treeList() {
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        List<CategoryEntity> treeList = entityList.stream()
                                                    .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                                                    .map(menu->{
                                                        menu.setChildren(getChildren(entityList,menu));
                                                        return menu;})
                                                    .sorted((menu1,menu2)->(menu1.getSort()==null?0:menu1.getSort())- (menu2.getSort()==null?0:menu2.getSort()))
                                                    .collect(Collectors.toList());

        return treeList;
    }

    @Override
    public void deleteByIds(List<Long> asList) {
        //TODO 删除之前，检查当前删除菜单是否被其他地方引用

        baseMapper.deleteBatchIds(asList);
    }

    /*
    * 设置分类子节点方法
    * */
    private List<CategoryEntity> getChildren(List<CategoryEntity> categoryEntityList,CategoryEntity parent){
        List<CategoryEntity> children = categoryEntityList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == parent.getCatId())
                .map(menu->{
                    menu.setChildren(getChildren(categoryEntityList,menu));
                    return menu;
                })
                .sorted((menu1,menu2)->(menu1.getSort()==null?0:menu1.getSort())- (menu2.getSort()==null?0:menu2.getSort()))
                .collect(Collectors.toList());
        return children;
    }

}