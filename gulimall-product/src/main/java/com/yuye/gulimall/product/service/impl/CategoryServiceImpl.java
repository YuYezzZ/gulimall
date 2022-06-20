package com.yuye.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.product.dao.CategoryDao;
import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.CategoryService;
import com.yuye.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redissonClient;

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
        List<CategoryEntity> treeList = entityList.parallelStream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
//                                                        menu.setChildren(getChildren(entityList,menu));
                    menu.setChildren(getChildren(menu));
                    return menu;
                })
                .sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .collect(Collectors.toList());
        return treeList;
    }

    @Override
    public void deleteByIds(List<Long> asList) {
        //TODO 删除之前，检查当前删除菜单是否被其他地方引用

        baseMapper.deleteBatchIds(asList);
    }

    @Override
    @Cacheable(value = {"catelog"},key = "'getLevel1Category'")
    public List<CategoryEntity> getLevel1Category() {
        log.info("查询了一次getLevel1Category");
        List<CategoryEntity> categoryEntities = null;
        categoryEntities = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, 0L));
        return categoryEntities;
//        String level1Catogory = stringRedisTemplate.opsForValue().get("level1Catogory");
//        List<CategoryEntity> categoryEntities=null;
//        if(!StringUtils.isEmpty(level1Catogory)){
//            categoryEntities = JSON.parseArray(level1Catogory, CategoryEntity.class);
//            return categoryEntities;
//        }else {
//            RLock lock = redissonClient.getLock("level1CatogoryLock");
//            lock.lock(10,TimeUnit.SECONDS);
//            String level1Catogory2 = stringRedisTemplate.opsForValue().get("level1Catogory");
//            if(!StringUtils.isEmpty(level1Catogory2)) {
//                categoryEntities = JSON.parseArray(level1Catogory2, CategoryEntity.class);
//                return categoryEntities;
//            }
//            try {
//                categoryEntities = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, 0L));
//                stringRedisTemplate.opsForValue().set("level1Catogory",JSON.toJSONString(categoryEntities),1,TimeUnit.DAYS);
//            }catch (Exception e){
//                log.debug("查询一级菜单异常:{}",e);
//            }finally {
//                lock.unlock();
//            }
//            return categoryEntities;
//        }
    }

    /*
     *
     * 获取二级菜单map
     * */
    @Override
    @Cacheable(value = {"catelog"},key ="'getCatelogJson'" )
    public Map<String, List<Catelog2Vo>> getCatelogJson() {

        List<CategoryEntity> all = this.baseMapper.selectList(null);
        Map<String, List<Catelog2Vo>> catelogMap = null;
        log.info("查询了一次getCatelogJson");
        List<CategoryEntity> level1Category = this.getByParetenCid(all, 0L);
        catelogMap = level1Category.parallelStream().collect(Collectors.toMap(key ->
                        key.getCatId().toString()
                , v -> {
                    String catelog1Id = v.getCatId().toString();
                    Long catId = v.getCatId();
                    List<CategoryEntity> categoryEntities = this.getByParetenCid(all, catId);
                    List<Catelog2Vo> catelog2Vos = categoryEntities.parallelStream().map(item -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo();
                        catelog2Vo.setCatelog1Id(catelog1Id);
                        catelog2Vo.setName(item.getName());
                        catelog2Vo.setId(item.getCatId().toString());
                        Long itemCatId = item.getCatId();
                        List<CategoryEntity> childs = this.getByParetenCid(all, itemCatId);
                        List<Catelog2Vo.Category3Vo> category3Vos = childs.parallelStream().map(child -> {
                            Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo();
                            category3Vo.setCatelog2Id(itemCatId.toString());
                            category3Vo.setId(child.getCatId().toString());
                            category3Vo.setName(child.getName());
                            return category3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatelog3List(category3Vos);
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                    return catelog2Vos;
                }));
        return catelogMap;
//        String catelogJson = stringRedisTemplate.opsForValue().get("catelogJson");
//        Map<String,List<Catelog2Vo>> catelogMap=null;
//        if(!StringUtils.isEmpty(catelogJson)){
//            log.info("命中缓存");
//            catelogMap = JSON.parseObject(catelogJson, new TypeReference<Map<String,List<Catelog2Vo>>>(){});
//            return catelogMap;
//        }
//        else {
//            RLock lock = redissonClient.getLock("catelogJsonLock");
//            lock.lock(10, TimeUnit.SECONDS);
//            log.info("获取到锁了");
//            String catelogJson2 = stringRedisTemplate.opsForValue().get("catelogJson");
//            if(!StringUtils.isEmpty(catelogJson2)){
//                log.info("二次命中缓存");
//                catelogMap = JSON.parseObject(catelogJson, new TypeReference<Map<String,List<Catelog2Vo>>>(){});
//                return catelogMap;
//            }
//            try{
//                List<CategoryEntity> all = this.baseMapper.selectList(null);
//                log.info("查询了一次数据库");
//                List<CategoryEntity> level1Category = this.getByParetenCid(all,0L);
//                catelogMap = level1Category.parallelStream().collect(Collectors.toMap(key ->
//                                key.getCatId().toString()
//                        , v -> {
//                            String catelog1Id = v.getCatId().toString();
//
//                            Long catId = v.getCatId();
//                            List<CategoryEntity> categoryEntities = this.getByParetenCid(all, catId);
//                            List<Catelog2Vo> catelog2Vos = categoryEntities.parallelStream().map(item -> {
//                                Catelog2Vo catelog2Vo = new Catelog2Vo();
//                                catelog2Vo.setCatelog1Id(catelog1Id);
//                                catelog2Vo.setName(item.getName());
//                                catelog2Vo.setId(item.getCatId().toString());
//                                Long itemCatId = item.getCatId();
//                                List<CategoryEntity> childs = this.getByParetenCid(all,itemCatId);
//                                List<Catelog2Vo.Category3Vo> category3Vos = childs.parallelStream().map(child -> {
//                                    Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo();
//                                    category3Vo.setCatelog2Id(itemCatId.toString());
//                                    category3Vo.setId(child.getCatId().toString());
//                                    category3Vo.setName(child.getName());
//                                    return category3Vo;
//                                }).collect(Collectors.toList());
//                                catelog2Vo.setCatelog3List(category3Vos);
//                                return catelog2Vo;
//                            }).collect(Collectors.toList());
//                            return catelog2Vos;
//                        }));
//                stringRedisTemplate.opsForValue().set("catelogJson",JSON.toJSONString(catelogMap),1,TimeUnit.DAYS);
//            }
//            catch (Exception e){
//                log.error("缓存异常：{}",e);
//            }
//            finally {
//                lock.unlock();
//            }
//            return catelogMap;
//        }
    }

    /*
     * 设置分类子节点方法
     * */
    private List<CategoryEntity> getChildren(/*List<CategoryEntity> categoryEntityList,*/CategoryEntity parent) {
//        List<CategoryEntity> children = categoryEntityList.parallelStream()
//                .filter(categoryEntity -> categoryEntity.getParentCid() == parent.getCatId())
//                .map(menu->{
//                    menu.setChildren(getChildren(categoryEntityList,menu));
//                    System.out.println(menu);
//                    return menu;
//                })
//                .sorted((menu1,menu2)->(menu1.getSort()==null?0:menu1.getSort())- (menu2.getSort()==null?0:menu2.getSort()))
//                .collect(Collectors.toList());
        List<CategoryEntity> children = baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, parent.getCatId()));
        children = children.parallelStream().map(menu -> {
            menu.setChildren(getChildren(menu));
            return menu;
        }).collect(Collectors.toList());
        return children;
    }

    /*
     * 根据parentid从所有数据中查出对应的数据
     * */
    private List<CategoryEntity> getByParetenCid(List<CategoryEntity> all, Long parentCid) {
        List<CategoryEntity> result = all.parallelStream().filter(item -> item.getParentCid() == parentCid).collect(Collectors.toList());

        return result;
    }
}