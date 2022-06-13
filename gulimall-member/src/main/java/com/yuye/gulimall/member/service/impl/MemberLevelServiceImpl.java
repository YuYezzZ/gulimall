package com.yuye.gulimall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuye.gulimall.common.utils.PageUtils;
import com.yuye.gulimall.common.utils.Query;
import com.yuye.gulimall.member.dao.MemberLevelDao;
import com.yuye.gulimall.member.entity.MemberLevelEntity;
import com.yuye.gulimall.member.service.MemberLevelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String  key = (String) params.get("key");
        LambdaQueryWrapper<MemberLevelEntity> memberLevelEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            memberLevelEntityLambdaQueryWrapper.like(MemberLevelEntity::getName,key).or().like(MemberLevelEntity::getId,key);
        }
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                memberLevelEntityLambdaQueryWrapper
        );

        return new PageUtils(page);
    }

}