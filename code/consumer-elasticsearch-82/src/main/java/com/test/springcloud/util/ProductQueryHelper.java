package com.test.springcloud.util;

import com.test.springcloud.dto.ProductQueryDTO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.StringUtils;

public class ProductQueryHelper {
    public static QueryBuilder wrapperQuery(ProductQueryDTO query) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //词条间隔（0完全匹配词条）
        int slop = 0;
        //品牌
        if (!StringUtils.isEmpty(query.getCategory())) {
            queryBuilder.filter(QueryBuilders.matchPhraseQuery("category", query.getCategory()).slop(slop));
        }

        //模糊匹配
        //词条间隔（1模糊匹配）
        if (!StringUtils.isEmpty(query.getTitle())) {
            queryBuilder.filter(QueryBuilders.matchPhraseQuery("title", query.getTitle()).slop(1));
        }

        return queryBuilder;
    }
}
