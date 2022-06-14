package com.yuye.gulimall.product.controller;

import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.product.convert.AttrEntityConvert;
import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.entity.ProductAttrValueEntity;
import com.yuye.gulimall.product.service.AttrAttrgroupRelationService;
import com.yuye.gulimall.product.service.AttrService;
import com.yuye.gulimall.product.service.ProductAttrValueService;
import com.yuye.gulimall.product.vo.AttrFormVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品属性
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:54:03
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
        AttrFormVO attr = attrService.getAttrFormVOById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrEntity attr){
		attrService.save(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrFormVO attrFormVO){
        Long attrGroupId = attrFormVO.getAttrGroupId();
        Long attrId = attrFormVO.getAttrId();
        if(attrGroupId!=null){
            attrAttrgroupRelationService.updateByAttrId(attrId,attrGroupId);
        }
        AttrEntity attrEntity = AttrEntityConvert.INSTANCE.attrFormVO2AttrEntityDTO(attrFormVO);
        attrService.updateById(attrEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }
    /*
    * 获取分类规格参数
    * */
    @RequestMapping("/base/list/{catelogId}")
    public  R baseList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        PageUtils page = attrService.queryPageByBase(params,catelogId);
        return R.ok().put("page",page);
    }
    /*
     * 获取分类销售属性
     * */
    @RequestMapping("/sale/list/{catelogId}")
    public  R saleList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        PageUtils page = attrService.queryPageBySale(params,catelogId);
        return R.ok().put("page",page);
    }

    /*
     * 获取spu规格
     * */
    @RequestMapping("/base/listforspu/{spuId}")
    public  R baseListForSpu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.getBySpuId(spuId);
        return R.ok().put("data",productAttrValueEntities);
    }
}
