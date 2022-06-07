package com.yuye.gulimall.product.controller;


import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.product.entity.AttrGroupEntity;
import com.yuye.gulimall.product.service.AttrGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:54:03
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catId") Long catId) {
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPageId(params, catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> catelogIds = attrGroupService.getParentIds(attrGroup.getCatelogId());
        return R.ok().put("attrGroup", attrGroup).put("catelogIds", catelogIds);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
