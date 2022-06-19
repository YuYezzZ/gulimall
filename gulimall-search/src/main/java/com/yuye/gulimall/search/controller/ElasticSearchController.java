package com.yuye.gulimall.search.controller;

import com.yuye.gulimall.common.to.SkuEsModelTO;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.search.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: yuye
 * @Date: 2022/6/17 - 06 - 17 - 21:13
 * @Description: com.yuye.gulimall.search.controller
 * @version: 1.0
 */
@RestController()
@RequestMapping("/search")
public class ElasticSearchController {
    @Autowired
    private ElasticSearchService elasticSearchService;
    @PostMapping("/product/save")
    public R saveProduct(@RequestBody List<SkuEsModelTO> skuEsModelTOS) throws SQLException, IOException {
        boolean b = elasticSearchService.saveProduct(skuEsModelTOS);
        if(b)
            return R.ok();
        return R.error("商品插入索引失败");
    }
}
