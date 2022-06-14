package com.yuye.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.ware.entity.PurchaseEntity;
import com.yuye.gulimall.ware.vo.MergeVO;

import java.util.Map;

/**
 * 采购信息
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 09:21:16
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils unreceiveList(Map<String, Object> params);

    void merge(MergeVO mergeVO);
}

