package com.example.service;

import cn.hutool.json.JSONUtil;
import com.example.domain.Product;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final RestHighLevelClient client;
    private static final String INDEX = "products"; // 索引名称

    /**
     * 添加产品
     */
    public String addProduct(Product product) throws IOException {
        IndexRequest request = new IndexRequest(INDEX)
                .id(product.getId())
                .source(JSONUtil.toJsonStr(product), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        return response.toString();
    }

    /**
     * 获取产品
     */
    public Product getProduct(String id) throws IOException {
        GetRequest request = new GetRequest(INDEX, id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        if (!response.isExists()) {
            return null;
        }
        return JSONUtil.toBean(response.getSourceAsString(), Product.class);
    }

    /**
     * 更新产品
     */
    public String updateProduct(Product product) throws IOException {
        UpdateRequest request = new UpdateRequest(INDEX, product.getId());
        request.doc(JSONUtil.toJsonStr(product), XContentType.JSON);
        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        return update.toString();
    }

    /**
     * 删除产品
     */
    public String deleteProduct(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX, id);
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        return delete.toString();
    }

    /*
    ------------------ es搜索服务------------------
     */

    public String searchProduct(String keyword) {
        // TODO
        // 构建搜索请求
        SearchRequest searchRequest = new SearchRequest(INDEX);
        // 搜索条件
        searchRequest.source().query(QueryBuilders.matchQuery("name", keyword));
        try {
            // 执行搜索
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            parseSearchResponse(response);
            return JSONUtil.toJsonStr(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //解析搜索响应
    public void parseSearchResponse(SearchResponse response) {
        // 获取搜索结果
        SearchHits hits = response.getHits();
        // 获取总记录数
        long totalHits = hits.getTotalHits().value;
        System.out.println("总记录数：" + totalHits);
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 获取文档内容
            String sourceAsString = hit.getSourceAsString();
            // 解析文档内容
            Product product = JSONUtil.toBean(sourceAsString, Product.class);
            // 处理解析后的结果
            System.out.println(product.toString());
        }
    }
}
