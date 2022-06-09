package com.yuye.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuye.gulimall.product.convert.AttrEntityConvert;
import com.yuye.gulimall.product.dao.CategoryDao;
import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.entity.BrandEntity;
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.BrandService;
import com.yuye.gulimall.product.service.CategoryService;
import com.yuye.gulimall.product.vo.AttrBaseVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 19:25
 * @Description: com.yuye.gulimall.product
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDao categoryDao;
    
    //测试品牌插入功能
    @Test
    public void insert(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandEntity.setDescript("万物互联");
        boolean save = brandService.save(brandEntity);
        System.out.println(save);
    }

    @Test
    public void query(){
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        LambdaQueryWrapper<BrandEntity> entityLambdaQueryWrapper = new LambdaQueryWrapper<BrandEntity>().eq(BrandEntity::getBrandId, brandEntity.getBrandId());
        BrandEntity one = brandService.getOne(entityLambdaQueryWrapper);
        System.out.println(one);
    }
    @Test
    public void findParentIdstest() {
        List<CategoryEntity> list = categoryService.list();
        list.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() > 0)
                .forEach(
                        item -> {
                            if (item.getCatId() == 34L) {
                                Long parentCid=item.getParentCid();
                                System.out.println(parentCid);
//                                findParentIdstest(parentCid, ids);
                            }
                        }
                );
    }

    @Test
    public void testAttrConvert(){
        AttrEntity attrEntity = new AttrEntity();
        attrEntity.setAttrId(3L);
        attrEntity.setAttrName("你好");
        attrEntity.setCatelogId(10L);
        AttrBaseVO attrBaseVO = AttrEntityConvert.INSTANCE.attrEntityDTO2AttrBaseVO(attrEntity);
        System.out.println(attrBaseVO);
    }

    @Test
    public void testSelectPage(){
        IPage<CategoryEntity> page = categoryDao.selectPage(new Page<>(1, 10), null);
        System.out.println(page.getRecords());
    }

}
