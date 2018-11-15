package cn.amaging.encology.es.service;

import cn.amaging.encology.es.common.SearchConstants;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DuQiyu on 2018/6/19.
 */
@PropertySource(value = "classpath:field.properties")
public abstract class AbstractService<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    @Resource
    protected ElasticsearchTemplate template;

    // 指定需要进行全文检索的域
    @Value("${field.content}")
    protected String[] FIELD_CONTENT;

    /**
     * 全文检索查询
     * */
    protected NativeSearchQueryBuilder fullQuery(Map<String, String> params) {
        if (null == params) {
            params = new HashMap<>();
        }
        // 查询条件分割为常规查询条件和扩展查询条件
        Map<String, String>[] maps = this.splitParams(params);
        // 使用扩展查询条件初始化查询构造器
        NativeSearchQueryBuilder builder = this.initQueryBuilder(maps[1]);
        // 常规查询条件处理
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String type = maps[0].get(SearchConstants.TYPE);
        if (StringUtils.isNotBlank(type)) { // 自定义类型（注：不是es自带的_type，_type将在es7以后废除，这里通过自定义type进行业务类型区分）
            boolQueryBuilder.must(QueryBuilders.termQuery(SearchConstants.TYPE, type));
        }
        // 全文检索查询条件分析
        String cond = maps[0].get(SearchConstants.COND);
        boolQueryBuilder = this.fullTextQueryBuilder(boolQueryBuilder, cond, FIELD_CONTENT);
        builder.withFilter(boolQueryBuilder);
        return builder;
    }

    /**
     * 条件查询
     * */
    protected NativeSearchQueryBuilder conditionQuery(Map<String, String> params) {
        if (null == params) {
            params = new HashMap<>();
        }
        // 查询条件分割为常规查询条件和扩展查询条件
        Map<String, String>[] maps = this.splitParams(params);
        NativeSearchQueryBuilder builder = this.initQueryBuilder(maps[1]);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> entry : maps[0].entrySet()) {
            if (null != entry.getValue() && StringUtils.isNotBlank(entry.getValue())) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(entry.getKey(), entry.getValue()));
            }
        }
        builder.withFilter(boolQueryBuilder);
        return builder;
    }

    /**
     * 根据uid进行查询
     * */
    protected NativeSearchQueryBuilder detailQuery(String uid) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withFilter(QueryBuilders.termQuery(SearchConstants.UID, uid));
        return builder;
    }

    /**
     * 查询条件分割器
     * 将常规查询条件和扩展查询条件分离
     * 常规查询条件，如：全文检索查询条件，指定字段查询条件等
     * 扩展查询条件，如：索引/排序/分页/范围查询/指定返回域等
     * (上层已经做了params非空判断，这里不再判断)
     * */
    private Map<String, String>[] splitParams(Map<String, String> params) {
        Map<String, String> extentParams = new HashMap<>();
        extentParams.put(SearchConstants.INDEX, params.get(SearchConstants.INDEX));
        params.remove(SearchConstants.INDEX);
        extentParams.put(SearchConstants.BY, params.get(SearchConstants.BY));
        params.remove(SearchConstants.BY);
        extentParams.put(SearchConstants.GTE, params.get(SearchConstants.GTE));
        params.remove(SearchConstants.GTE);
        extentParams.put(SearchConstants.LTE, params.get(SearchConstants.LTE));
        params.remove(SearchConstants.LTE);
        extentParams.put(SearchConstants.FIELDS, params.get(SearchConstants.FIELDS));
        params.remove(SearchConstants.FIELDS);
        extentParams.put(SearchConstants.SORT, params.get(SearchConstants.SORT));
        params.remove(SearchConstants.SORT);
        extentParams.put(SearchConstants.PAGE_NUMBER, params.get(SearchConstants.PAGE_NUMBER));
        params.remove(SearchConstants.PAGE_NUMBER);
        extentParams.put(SearchConstants.PAGE_SIZE, params.get(SearchConstants.PAGE_SIZE));
        params.remove(SearchConstants.PAGE_SIZE);
        return (Map<String, String>[]) new Map[]{params, extentParams};
    }

    /**
     * 使用扩展查询条件进行初始化
     * @param extendParams
     * */
    private NativeSearchQueryBuilder initQueryBuilder(Map<String, String> extendParams) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 按索引查询
        String index = extendParams.get(SearchConstants.INDEX);
        if (StringUtils.isNotBlank(index)) { // 自定义索引，支持多索引，“,”隔开，如ahcmcc*,sncmcc*
            builder.withIndices(index.split(SearchConstants.SPLIT_COMMA));
        }
        // 条件范围查询
        builder.withQuery(this.rangeQuery(extendParams));
        // 自定义返回域
        String fields = extendParams.get(SearchConstants.FIELDS);
        if (StringUtils.isNotBlank(fields)) {
            builder.withFields(fields.split(SearchConstants.SPLIT_COMMA));
        }
        // 自定义排序
        List<SortBuilder> sortBuilderList = this.sort(extendParams);
        for (SortBuilder sortBuilder : sortBuilderList) {
            builder.withSort(sortBuilder);
        }
        // 自定义分页，默认：单页50条
        builder.withPageable(this.page(extendParams));
        return builder;
    }

    /**
     * 全文检索查询条件构造
     * @param boolQueryBuilder 查询对象
     * @param cond 查询条件，支持多条件查询，使用空格进行分割，如a b
     * @param fields 查询域，只在这些域里进行查询
     * */
    protected BoolQueryBuilder fullTextQueryBuilder(BoolQueryBuilder boolQueryBuilder, String cond, String... fields) {
        if (StringUtils.isBlank(cond)) {
            return boolQueryBuilder;
        }
        for (String str : cond.split(" ")) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            if (str.charAt(0) == SearchConstants.NOT) {
                boolQueryBuilder.mustNot(QueryBuilders.multiMatchQuery(str.substring(1), fields).type(MultiMatchQueryBuilder.Type.PHRASE));
            } else {
                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(str, fields).type(MultiMatchQueryBuilder.Type.PHRASE));
            }
        }
        return boolQueryBuilder;
    }

    // 目前只支持单条件范围查询
    protected RangeQueryBuilder rangeQuery(Map<String, String> params) {
        String by = params.get(SearchConstants.BY);
        if (StringUtils.isBlank(by)) {
            return null;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(by);
        String gte = params.get(SearchConstants.GTE);
        if (StringUtils.isNotBlank(gte)) {
            rangeQueryBuilder.from(gte);
            rangeQueryBuilder.includeLower(true);
        }
        String lte = params.get(SearchConstants.LTE);
        if (StringUtils.isNotBlank(lte)) {
            rangeQueryBuilder.to(lte);
            rangeQueryBuilder.includeUpper(true);
        }
        return rangeQueryBuilder;
    }

    protected PageRequest page(Map<String, String> params) {
        int pageNumber = StringUtils.isBlank(params.get(SearchConstants.PAGE_NUMBER)) ? 0 : Integer.parseInt(params.get(SearchConstants.PAGE_NUMBER));
        int pageSize = StringUtils.isBlank(params.get(SearchConstants.PAGE_SIZE)) ? 50 : Integer.parseInt(params.get(SearchConstants.PAGE_SIZE));
        return PageRequest.of(pageNumber, pageSize);
    }

    protected List<SortBuilder> sort(Map<String, String> params) {
        List<SortBuilder> list = new ArrayList<>();
        String sorts = params.get(SearchConstants.SORT);
        if (StringUtils.isBlank(sorts)) {
            sorts = SearchConstants.SORT_DEFAULT_FIELD;
        }
        String[] array = sorts.split(SearchConstants.SPLIT_COMMA);
        for (String sort : array) {
            SortOrder sortOrder = SortOrder.ASC;
            sort = sort.trim();
            char first = sort.charAt(0);
            if (first == SearchConstants.SORT_DESC) {
                sortOrder = SortOrder.DESC;
            }
            if (first == SearchConstants.SORT_ASC || first == SearchConstants.SORT_DESC) {
                sort = sort.substring(1);
            }
            list.add(SortBuilders.fieldSort(sort).order(sortOrder));
        }
        return list;
    }
}
