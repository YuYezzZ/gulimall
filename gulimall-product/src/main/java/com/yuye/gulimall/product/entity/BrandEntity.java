package com.yuye.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuye.gulimall.common.volidation.OneOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

/**
 * 品牌
 * 
 * @author yuye
 * @email 962701261@qq.com
 * @date 2022-05-26 16:20:00
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotEmpty(message = "品牌名称不能为空")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@URL(message = "品牌地址不合法")
	private String logo;
	/**
	 * 介绍
	 */
	@NotNull(message = "描述不能为空")
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@OneOrZero
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "首字母不能为空")
	@Pattern(regexp = "^[a-zA-Z]$",message = "首字母为一个字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotEmpty(message = "排序母不能为空")
	@PositiveOrZero(message = "排序为非负整数")
	private Integer sort;

}
