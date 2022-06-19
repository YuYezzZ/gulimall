package com.yuye.gulimall.product.app;


import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.product.entity.CategoryBrandRelationEntity;
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.CategoryBrandRelationService;
import com.yuye.gulimall.product.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:54:03
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    /*
    * 查询所有分类，并按树形结构分层
    * */
    @RequestMapping("/list/tree")
    public R treeList(){
        List<CategoryEntity> treeList = categoryService.treeList();
        return  R.ok().put("data",treeList);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("category", category);
    }

    /**
     * 保存
     */

    @Caching(evict={
            @CacheEvict(value = {"catelog"},key = "'getCatelogJson'"),
            @CacheEvict(value = {"catelog"},key = "'getLevel1Category'")
    })
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @Caching(evict={
            @CacheEvict(value = {"catelog"},key = "'getCatelogJson'"),
            @CacheEvict(value = {"catelog"},key = "'getLevel1Category'")
    })
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){

		categoryService.updateById(category);
        if(!StringUtils.isEmpty(category.getName())){
            CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
            categoryBrandRelationEntity.setCatelogId(category.getCatId());
            categoryBrandRelationEntity.setCatelogName(category.getName());
            categoryBrandRelationService.updateDetail(categoryBrandRelationEntity);
        }

        return R.ok();
    }

    /**
     * 删除
     */
    @Caching(evict={
            @CacheEvict(value = {"catelog"},key = "'getCatelogJson'"),
            @CacheEvict(value = {"catelog"},key = "'getLevel1Category'")
    })
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.deleteByIds(Arrays.asList(catIds));
        return R.ok();
    }
    /*
    * 分类菜单拖拽工能实现
    * */
    @PostMapping("/list/drop")
    @Caching(evict={
            @CacheEvict(value = {"catelog"},key = "'getCatelogJson'"),
            @CacheEvict(value = {"catelog"},key = "'getLevel1Category'")
    })
    //@RequiresPermissions("product:category:delete")
    public R drop(@RequestBody List<CategoryEntity> categoryEntities){
        categoryService.updateBatchById(categoryEntities);
        return R.ok();
    }

}
