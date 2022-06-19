package com.yuye.gulimall.product.web;

import com.yuye.gulimall.product.entity.CategoryEntity;
import com.yuye.gulimall.product.service.CategoryService;
import com.yuye.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Auther: yuye
 * @Date: 2022/6/17 - 06 - 17 - 22:46
 * @Description: com.yuye.gulimall.product.web
 * @version: 1.0
 */
@Controller
@Slf4j
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String index(Model model){
        List<CategoryEntity> categoryEntities =categoryService.getLevel1Category();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @GetMapping("/index/catelog.json")
    @ResponseBody

    public Map<String,  List<Catelog2Vo>> getCatelogJson(){
        Map<String, List<Catelog2Vo>> map =  categoryService.getCatelogJson();
        return map ;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello" ;
    }
}
