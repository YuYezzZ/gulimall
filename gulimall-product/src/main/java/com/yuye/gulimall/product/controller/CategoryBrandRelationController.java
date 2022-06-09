package com.yuye.gulimall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.product.entity.CategoryBrandRelationEntity;
import com.yuye.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;




/**
 * 品牌分类关联
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:54:03
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    /**
     * 列表
     */
    @RequestMapping("/catelog/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Long brandId){
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId, brandId));


        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);
        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveName(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 沉余更新
     */
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R updateDetail(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        categoryBrandRelationService.updateDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据catId查询品牌
     */
    @RequestMapping("/brands/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R brandList(@RequestParam Long catId){
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = categoryBrandRelationService.brandList(catId);

        return R.ok().put("data", categoryBrandRelationEntities);
    }
}
