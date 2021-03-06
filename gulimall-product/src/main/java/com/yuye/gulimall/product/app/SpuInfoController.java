package com.yuye.gulimall.product.app;

import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.product.entity.SpuInfoEntity;
import com.yuye.gulimall.product.service.SpuInfoService;
import com.yuye.gulimall.product.vo.SpuSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * spu信息
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:54:03
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.save(spuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * 删除
     */
    @PostMapping("/save/all")
    //@RequiresPermissions("product:spuinfo:save")
    public R saveSpuInfo(@RequestBody SpuSaveVO spuSaveVO){
        spuInfoService.saveSpuInfo(spuSaveVO);

        return R.ok();
    }

    /**
     * 商品上架
     */
    @PostMapping("/{spuId}/up")
    //@RequiresPermissions("product:spuinfo:save")
    public R up(@PathVariable Long spuId){
        spuInfoService.up(spuId);
        return R.ok();
    }
}
