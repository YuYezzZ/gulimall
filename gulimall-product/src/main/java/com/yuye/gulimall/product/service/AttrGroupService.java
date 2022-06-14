package com.yuye.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.entity.AttrGroupEntity;
import com.yuye.gulimall.product.vo.WithattrVO;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageId(Map<String, Object> params, Long catId);

    List<Long> getParentIds(Long catelogId);

    List<AttrEntity> attrRelation(Long attrGroupId);

    void removeList(List<AttrAttrgroupRelationEntity> list);

    PageUtils noattrRelation(Map<String, Object> params, Long attrGroupId);

    void saveList(List<AttrAttrgroupRelationEntity> list);

    List<WithattrVO> selectWithattr(Long catelogId);

}

