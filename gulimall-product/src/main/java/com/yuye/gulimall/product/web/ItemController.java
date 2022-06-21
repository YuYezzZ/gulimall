package com.yuye.gulimall.product.web;

import com.yuye.gulimall.product.service.SkuInfoService;
import com.yuye.gulimall.product.vo.SkuItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @Auther: yuye
 * @Date: 2022/6/21 - 06 - 21 - 8:43
 * @Description: com.yuye.gulimall.product.web
 * @version: 1.0
 */
@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    @GetMapping("/{skuId}.html")
    public String item(@PathVariable Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVO skuItemVO = skuInfoService.getItem(skuId);
        model.addAttribute("item",skuItemVO);
        return "item";
    }
}
