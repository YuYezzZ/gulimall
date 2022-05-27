package com.yuye.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 09:22:00
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

