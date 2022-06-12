package com.yuye.gulimall.product.convert;

import com.yuye.gulimall.product.entity.AttrGroupEntity;
import com.yuye.gulimall.product.vo.WithattrVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:55
 * @Description: com.yuye.gulimall.product.convert
 * @version: 1.0
 */
@Mapper
public interface AttrGroupEntityConvert {
    AttrGroupEntityConvert INSTANCE = Mappers.getMapper(AttrGroupEntityConvert.class);
    List<WithattrVO> AttrGroupList2WithattrVOList(List<AttrGroupEntity> attrGroupEntities);
}
