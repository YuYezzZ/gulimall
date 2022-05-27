package com.yuye.gulimall.member.dao;

import com.yuye.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 09:22:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
