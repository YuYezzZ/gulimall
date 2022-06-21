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
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: yuye
 * @Date: 2022/5/26 - 05 - 26 - 19:25
 * @Description: com.yuye.gulimall.product
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GulimallProductApplicationTests {
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Test
    //测试验证码服务
    public void sms(){


    }
    //测试线程池是否配置
    @Test
    public void executorConn(){

        log.info("线程池参数:{}",threadPoolExecutor.getCorePoolSize());
        log.info("线程池参数:{}",threadPoolExecutor.getQueue());
        log.info("线程池参数:{}",threadPoolExecutor.getMaximumPoolSize());
        log.info("线程池参数:{}",threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS));
    }
    //测试redis是否成功连接
    @Test
    public void redisSet(){
        redisTemplate.opsForValue().set("aaa","dd");
        String aaa = redisTemplate.opsForValue().get("aaa");
        log.info("aaa:{}",aaa);
    }

    //测试redis是否成功连接
    @Test
    public void redisConn(){
        log.info("连接对象：{}",redisTemplate);
    }

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
