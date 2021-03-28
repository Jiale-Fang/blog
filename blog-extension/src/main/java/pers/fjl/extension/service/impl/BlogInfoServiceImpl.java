package pers.fjl.extension.service.impl;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;
import pers.fjl.common.entity.PageResult;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.extension.dao.BlogInfoMapper;
import pers.fjl.extension.po.BlogInfo;
import pers.fjl.extension.service.BlogInfoService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Resource
    private BlogInfoMapper blogInfoMapper;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    private SearchRequest searchRequest = new SearchRequest("blog-search");

    @Override
    public void importData() {
    }

    public PageResult homePage(QueryPageBean queryPageBean) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 分页
        // 从第几条数据开始
        sourceBuilder.from((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize());
        sourceBuilder.size(queryPageBean.getPageSize());
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 执行搜素
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            documentFields.getSourceAsMap().put("blogId", documentFields.getId());
            list.add(documentFields.getSourceAsMap());
        }
        PageResult pageResult = new PageResult();
        pageResult.setRecords(list);
        pageResult.setTotal(total("", false));
        return pageResult;
    }

    @Override
    public void save(BlogInfo blogInfo) {
        blogInfoMapper.save(blogInfo);
    }

    @Override
    public List<Map<String, Object>> searchPage(QueryPageBean queryPageBean) throws IOException {
        // 条件搜索
//        SearchRequest searchRequest = new SearchRequest("blog-search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 分页
        // 从第几条数据开始
        sourceBuilder.from((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize());
        sourceBuilder.size(queryPageBean.getPageSize());

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(queryPageBean.getQueryString(), "title", "content", "description", "type_name", "tag_name");
        sourceBuilder.query(multiMatchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 执行搜素
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            System.out.println("结果======》" + documentFields);
            documentFields.getSourceAsMap().put("id", documentFields.getId());
            list.add(documentFields.getSourceAsMap());
        }

        return list;
    }

    // 3、获取这些数据实现搜索高亮功能
    public PageResult highLightSearchPage(QueryPageBean queryPageBean) throws IOException {

        // 条件搜索
//        SearchRequest searchRequest = new SearchRequest("blog-search");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 分页
        // 从第几条数据开始
        sourceBuilder.from((queryPageBean.getCurrentPage() - 1) * queryPageBean.getPageSize());
        sourceBuilder.size(queryPageBean.getPageSize());

        // 精准匹配
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(queryPageBean.getQueryString(), "title", "content");
        sourceBuilder.query(multiMatchQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("content").field("title");
        highlightBuilder.requireFieldMatch(false); // 只高亮显示第一个即可
        highlightBuilder.preTags("<span style = 'color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        // 执行搜素
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {  //hits中的字段没有被高亮，因此要将es返回highlight中的高亮字段替换到hit中
            // 解析高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            HighlightField content = highlightFields.get("content");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 原来的字段结果
            Map<String, Object> sourceAsMap1 = replaceHits(title, sourceAsMap, "title");
            Map<String, Object> sourceAsMap2 = replaceHits(content, sourceAsMap1, "content");
            sourceAsMap2.put("blogId", hit.getId());
            list.add(hit.getSourceAsMap());
        }
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total(queryPageBean.getQueryString(), true));
        pageResult.setRecords(list);
        return pageResult;
    }

    public Map<String, Object> replaceHits(HighlightField field, Map<String, Object> sourceAsMap, String type) {
        if (field != null) {   // 解析高亮字段，将原来字段替换为高亮字段
            Text[] fragments = field.fragments();
            String n_field = "";
            for (Text text : fragments) {
//                n_field += text;
                n_field += text;
                break;
            }
            sourceAsMap.put(type, n_field); //高亮字段替换掉原来内容
        }
        return sourceAsMap;
    }

    public int total(String queryString, boolean flag) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        if (flag) {
            // 精准匹配
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(queryString, "title", "content");
            sourceBuilder.query(multiMatchQueryBuilder);
        }
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 执行搜素
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return (int) searchResponse.getHits().getTotalHits().value;
    }
}
