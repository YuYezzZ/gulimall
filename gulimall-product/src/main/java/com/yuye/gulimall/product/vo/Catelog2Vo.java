package com.yuye.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-08 14:50
 *
 * 二级分类id
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 一级父分类的id
     */
    private String catelog1Id;

    /**
     * 三级子分类
     */
    private List<Category3Vo> catelog3List;

    private String id;

    private String name;


    /**
     * 三级分类vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category3Vo implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 父分类、二级分类id
         */
        private String catelog2Id;

        private String id;

        private String name;
    }

}
