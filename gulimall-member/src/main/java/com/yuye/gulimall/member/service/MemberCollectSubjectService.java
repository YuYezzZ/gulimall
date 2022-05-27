package com.yuye.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.member.entity.MemberCollectSubjectEntity;

import java.util.Map;

/**
 * 会员收藏的专题活动
 *
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 09:22:00
 */
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

