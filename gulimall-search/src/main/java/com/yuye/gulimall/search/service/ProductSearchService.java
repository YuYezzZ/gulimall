package com.yuye.gulimall.search.service;

import com.yuye.gulimall.search.vo.SearchParam;
import com.yuye.gulimall.search.vo.SearchResult;

import java.io.IOException;

/**
 * @Auther: yuye
 * @Date: 2022/6/20 - 06 - 20 - 11:06
 * @Description: com.yuye.gulimall.search.service
 * @version: 1.0
 */
public interface ProductSearchService {
    SearchResult search(SearchParam searchParam) throws IOException;
}
