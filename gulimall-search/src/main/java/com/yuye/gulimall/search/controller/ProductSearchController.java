package com.yuye.gulimall.search.controller;

import com.yuye.gulimall.search.service.ProductSearchService;
import com.yuye.gulimall.search.vo.SearchParam;
import com.yuye.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Auther: yuye
 * @Date: 2022/6/20 - 06 - 20 - 11:06
 * @Description: com.yuye.gulimall.search.controller
 * @version: 1.0
 */
@Controller
public class ProductSearchController {
    @Autowired
    ProductSearchService productSearchService;
    @GetMapping({"/","/list.html"})
    public String index(SearchParam searchParam, Model model, HttpServletRequest request) throws IOException {
        searchParam.set_queryString(request.getQueryString());
        SearchResult result= productSearchService.search(searchParam);
        model.addAttribute("result",result);
        return "list";
    }
}
