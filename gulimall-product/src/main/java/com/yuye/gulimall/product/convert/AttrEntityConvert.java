package com.yuye.gulimall.product.convert;

import com.yuye.gulimall.product.entity.AttrEntity;
import com.yuye.gulimall.product.vo.AttrBaseVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * @Auther: yuye
 * @Date: 2022/6/8 - 06 - 08 - 19:10
 * @Description: com.yuye.gulimall.product.convert
 * @version: 1.0
 */
@Component
@Mapper
public interface AttrEntityConvert {
    AttrEntityConvert INSTANCE = Mappers.getMapper(AttrEntityConvert.class);
    AttrBaseVO attrEntityDTO2AttrBaseVO(AttrEntity attrEntity);
}
