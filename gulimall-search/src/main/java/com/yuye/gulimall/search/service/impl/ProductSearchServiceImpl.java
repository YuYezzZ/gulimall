package com.yuye.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.yuye.gulimall.common.to.SkuEsModelTO;
import com.yuye.gulimall.search.constant.ProductSearch;
import com.yuye.gulimall.search.service.ProductSearchService;
import com.yuye.gulimall.search.vo.SearchParam;
import com.yuye.gulimall.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: yuye
 * @Date: 2022/6/20 - 06 - 20 - 11:07
 * @Description: com.yuye.gulimall.search.service.impl
 * @version: 1.0
 */
@Service
@Slf4j
public class ProductSearchServiceImpl implements ProductSearchService {
    @Autowired
    RestHighLevelClient client;
    @Override
    public SearchResult search(SearchParam searchParam) throws IOException {
        SearchSourceBuilder searchSourceBuilder = buildSearchSource(searchParam);
        log.info("DSL语句：{}",searchSourceBuilder.toString());
        SearchRequest request = new SearchRequest(new String[]{ProductSearch.PRODUCT_INDEX},searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchResult searchResult = getSearchResult(response,searchParam);
        return searchResult;
    }
    /*
    * 解析响应结果方法
    * */
    private SearchResult getSearchResult(SearchResponse response,SearchParam searchParam){
        SearchResult result = new SearchResult();

        //1、返回的所有查询到的商品
        SearchHits hits = response.getHits();

        List<SkuEsModelTO> esModels = new ArrayList<>();
        //遍历所有商品信息
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModelTO esModel = JSON.parseObject(sourceAsString, SkuEsModelTO.class);

                //判断是否按关键字检索，若是就显示高亮，否则不显示
                if (!StringUtils.isEmpty(searchParam.getKeyword())) {
                    //拿到高亮信息显示标题
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String skuTitleValue = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(skuTitleValue);
                }
                esModels.add(esModel);
            }
        }
        result.setProduct(esModels);

        //2、当前商品涉及到的所有属性信息
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        //获取属性信息的聚合
        ParsedNested attrsAgg = response.getAggregations().get("attrsAgg");
        ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1、得到属性的id
            long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            //2、得到属性的名字
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            //3、得到属性的所有值
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValuesAgg");
            List<String> attrValues = attrValueAgg.getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);
        }

        result.setAttrs(attrVos);

        //3、当前商品涉及到的所有品牌信息
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        //获取到品牌的聚合
        ParsedLongTerms brandAgg = response.getAggregations().get("brandIdAgg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();

            //1、得到品牌的id
            long brandId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);

            //2、得到品牌的名字
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);

            //3、得到品牌的图片
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brandImgAgg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);

            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);

        //4、当前商品涉及到的所有分类信息
        //获取到分类的聚合
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        ParsedLongTerms catalogAgg = response.getAggregations().get("catelogAgg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            //得到分类id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            //得到分类名
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catelogNameAgg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalogName);
            catalogVos.add(catalogVo);
        }

        result.setCatalogs(catalogVos);
        //===============以上可以从聚合信息中获取====================//
        //5、分页信息-页码
        result.setPageNum(searchParam.getPageNum());
        //5、1分页信息、总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);

        //5、2分页信息-总页码-计算
        int totalPages = (int)total % ProductSearch.PRODUCT_PAGESIZE == 0 ?
                (int)total / ProductSearch.PRODUCT_PAGESIZE : ((int)total / ProductSearch.PRODUCT_PAGESIZE + 1);
        result.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
        return result;
    }

    /*
    * 构建DSL语句方法
    * */
    private SearchSourceBuilder buildSearchSource(SearchParam searchParam){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //全文检索字段
        if(!StringUtils.isEmpty(searchParam.getKeyword())){
            MatchQueryBuilder skuTitle = QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword());
            queryBuilder.must(skuTitle);
        }
        if(searchParam.getCatelog3Id()!=null){
            TermQueryBuilder catelogId = QueryBuilders.termQuery("catelogId", searchParam.getCatelog3Id());
            queryBuilder.filter(catelogId);
        }
        if(searchParam.getBrandId()!=null&&searchParam.getBrandId().size()>0){
            List<Long> brandIds = searchParam.getBrandId();
            TermsQueryBuilder brandId = QueryBuilders.termsQuery("brandId", brandIds);
            queryBuilder.filter(brandId);
        }
        if(searchParam.getHasStock()!=null){
            Integer hasStock = searchParam.getHasStock();
            TermQueryBuilder hasStockQuery = QueryBuilders.termQuery("hasStock", hasStock == 1 ? true : false);
            queryBuilder.filter(hasStockQuery);
        }
        if(!StringUtils.isEmpty(searchParam.getSkuPrice())){
            String skuPriceStr = searchParam.getSkuPrice();
            String[] skuPrice = skuPriceStr.split("_");
            if(skuPrice.length==1){
                RangeQueryBuilder skuPrice1 = QueryBuilders.rangeQuery("skuPrice").gte(skuPrice[1]);
                queryBuilder.filter(skuPrice1);
            }else{
                RangeQueryBuilder skuPrice1 = QueryBuilders.rangeQuery("skuPrice").gte(skuPrice[1]).lte(skuPrice[2]);
                queryBuilder.filter(skuPrice1);
            }
        }
        if(searchParam.getAttrs()!=null && searchParam.getAttrs().size()>0){
            List<String> attrs = searchParam.getAttrs();
            for (String attr : attrs) {
                String[] s = attr.split("_");
                Long attrId = Long.parseLong(s[0]);
                String[] attrValues = s[1].split(":");
                BoolQueryBuilder attrQuery = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("attrs.attrId", attrId)).must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", attrQuery, ScoreMode.None);
                queryBuilder.filter(nestedQuery);
            }
        }
        searchSourceBuilder.query(queryBuilder);
        if(!StringUtils.isEmpty(searchParam.getSort())){
            String sortStr = searchParam.getSort();
            String[] sort = sortStr.split("_");
            searchSourceBuilder.sort(sort[0], "asc".equals(sort[1])?SortOrder.ASC:SortOrder.DESC);
        }
        //高亮显示
        if(!StringUtils.isEmpty(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        //聚合查询
        TermsAggregationBuilder brandIdAgg = AggregationBuilders.terms("brandIdAgg").field("brandId").size(50);
        brandIdAgg.subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName").size(1));
        brandIdAgg.subAggregation(AggregationBuilders.terms("brandImgAgg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brandIdAgg);

        TermsAggregationBuilder catelogAgg = AggregationBuilders.terms("catelogAgg").field("catelogId").size(50);
        catelogAgg.subAggregation(AggregationBuilders.terms("catelogNameAgg").field("catelogName").size(1));
        searchSourceBuilder.aggregation(catelogAgg);

        NestedAggregationBuilder attrsNested = AggregationBuilders.nested("attrsAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValues").size(50));
        attrsNested.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(attrsNested);
        //分页
        searchSourceBuilder.from((searchParam.getPageNum()-1)* ProductSearch.PRODUCT_PAGESIZE).size(ProductSearch.PRODUCT_PAGESIZE);
        return searchSourceBuilder;
    }
}
