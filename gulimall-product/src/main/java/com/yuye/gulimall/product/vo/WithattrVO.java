package com.yuye.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/12 - 06 - 12 - 19:39
 * @Description: com.yuye.gulimall.product.vo
 * @version: 1.0
 */
@Data
public class WithattrVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /*
    * 关联关系
    * */
    private List<AttrFormVO> attrs;
}
