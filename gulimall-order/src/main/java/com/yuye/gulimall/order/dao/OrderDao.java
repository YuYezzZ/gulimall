package com.yuye.gulimall.order.dao;

import com.yuye.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-27 09:22:42
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
