package com.yuye.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuye.gulimall.common.to.SkuEsModelTO;
import com.yuye.gulimall.common.utils.R;
import com.yuye.gulimall.search.constant.ProductSearch;
import com.yuye.gulimall.search.feign.ProductFeignService;
import com.yuye.gulimall.search.service.ProductSearchService;
import com.yuye.gulimall.search.vo.AttrFormVO;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    @Autowired
    ProductFeignService productFeignService;
    @Override
    public SearchResult search(SearchParam searchParam) throws IOException {
        SearchSourceBuilder searchSourceBuilder = buildSearchSource(searchParam);
        log.info("DSL?????????{}",searchSourceBuilder.toString());
        SearchRequest request = new SearchRequest(new String[]{ProductSearch.PRODUCT_INDEX},searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchResult searchResult = getSearchResult(response,searchParam);
        return searchResult;
    }
    /*
    * ????????????????????????
    * */
    private SearchResult getSearchResult(SearchResponse response,SearchParam searchParam){
        SearchResult result = new SearchResult();

        //1????????????????????????????????????
        SearchHits hits = response.getHits();

        List<SkuEsModelTO> esModels = new ArrayList<>();
        //????????????????????????
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModelTO esModel = JSON.parseObject(sourceAsString, SkuEsModelTO.class);

                //????????????????????????????????????????????????????????????????????????
                if (!StringUtils.isEmpty(searchParam.getKeyword())) {
                    //??????????????????????????????
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String skuTitleValue = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(skuTitleValue);
                }
                esModels.add(esModel);
            }
        }
        result.setProduct(esModels);

        //2?????????????????????????????????????????????
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        //???????????????????????????
        ParsedNested attrsAgg = response.getAggregations().get("attrsAgg");
        ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1??????????????????id
            long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            //2????????????????????????
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);

            //3???????????????????????????
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
            List<String> attrValues = attrValueAgg.getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);
        }

        result.setAttrs(attrVos);

        //3?????????????????????????????????????????????
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        //????????????????????????
        ParsedLongTerms brandAgg = response.getAggregations().get("brandIdAgg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();

            //1??????????????????id
            long brandId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);

            //2????????????????????????
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);

            //3????????????????????????
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brandImgAgg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);

            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);

        //4?????????????????????????????????????????????
        //????????????????????????
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        ParsedLongTerms catalogAgg = response.getAggregations().get("catelogAgg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            //????????????id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            //???????????????
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catelogNameAgg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalogName);
            catalogVos.add(catalogVo);
        }

        result.setCatalogs(catalogVos);
        //===============????????????????????????????????????====================//
        //5???????????????-??????
        result.setPageNum(searchParam.getPageNum());
        //5???1???????????????????????????
        long total = hits.getTotalHits().value;
        result.setTotal(total);

        //5???2????????????-?????????-??????
        int totalPages = (int)total % ProductSearch.PRODUCT_PAGESIZE == 0 ?
                (int)total / ProductSearch.PRODUCT_PAGESIZE : ((int)total / ProductSearch.PRODUCT_PAGESIZE + 1);
        result.setTotalPages(totalPages);

        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);
        log.info("?????????????????????{}",result);
        //???????????????
        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            List<SearchResult.NavVo> collect = searchParam.getAttrs().stream().map(attr -> {
                //1??????????????????attrs?????????????????????
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.attrInfo(Long.parseLong(s[0]));
                if ("1".equals(r.get("code").toString())) {
                    Object o = r.get("attr");
                    ObjectMapper objectMapper = new ObjectMapper();
                    AttrFormVO data = objectMapper.convertValue(o, AttrFormVO.class);
                    navVo.setNavName(data.getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }

                //2???????????????????????????????????????????????????????????????????????????????????????url?????????????????????
                //??????????????????????????????????????????
                String encode = null;
                try {
                    encode = URLEncoder.encode(attr,"UTF-8");
                    encode.replace("+","%20");  //??????????????????????????????Java???????????????????????????
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String replace = searchParam.get_queryString().replace("&attrs=" + attr, "");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);

                return navVo;
            }).collect(Collectors.toList());

            result.setNavs(collect);
        }
        return result;
    }

    /*
    * ??????DSL????????????
    * */
    private SearchSourceBuilder buildSearchSource(SearchParam searchParam){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //??????????????????
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
                RangeQueryBuilder skuPrice1 = QueryBuilders.rangeQuery("skuPrice").gte(skuPrice[0]);
                queryBuilder.filter(skuPrice1);
            }else{
                RangeQueryBuilder skuPrice1 = QueryBuilders.rangeQuery("skuPrice").gte(skuPrice[0]).lte(skuPrice[1]);
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
        //????????????
        if(!StringUtils.isEmpty(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        //????????????
        TermsAggregationBuilder brandIdAgg = AggregationBuilders.terms("brandIdAgg").field("brandId").size(50);
        brandIdAgg.subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName").size(1));
        brandIdAgg.subAggregation(AggregationBuilders.terms("brandImgAgg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brandIdAgg);

        TermsAggregationBuilder catelogAgg = AggregationBuilders.terms("catelogAgg").field("catelogId").size(50);
        catelogAgg.subAggregation(AggregationBuilders.terms("catelogNameAgg").field("catelogName").size(1));
        searchSourceBuilder.aggregation(catelogAgg);

        NestedAggregationBuilder attrsNested = AggregationBuilders.nested("attrsAgg", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(50));
        attrsNested.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(attrsNested);
        //??????
        searchSourceBuilder.from((searchParam.getPageNum()-1)* ProductSearch.PRODUCT_PAGESIZE).size(ProductSearch.PRODUCT_PAGESIZE);
        return searchSourceBuilder;
    }
}
