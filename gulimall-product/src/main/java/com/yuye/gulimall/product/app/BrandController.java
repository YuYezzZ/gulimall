package com.yuye.gulimall.product.app;

import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.common.valid.AddGroup;
import com.yuye.gulimall.product.entity.BrandEntity;
import com.yuye.gulimall.product.entity.CategoryBrandRelationEntity;
import com.yuye.gulimall.product.service.BrandService;
import com.yuye.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 品牌
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:54:03
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand/*, BindingResult bindingResult*/){
        /*if(bindingResult.hasErrors()){
            HashMap<String, String> map = new HashMap<>();
            bindingResult.getFieldErrors().forEach(item->{
                String name = item.getField();
                String message = item.getDefaultMessage();
                map.put("name",name);
                map.put("message",message);
            });
            return R.ok().put("data",map);
        }*/
		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);
        if(!
                StringUtils.isEmpty(brand.getName())){
            CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
            categoryBrandRelationEntity.setBrandId(brand.getBrandId());
            categoryBrandRelationEntity.setBrandName(brand.getName());
            categoryBrandRelationService.updateDetail(categoryBrandRelationEntity);
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }
}
