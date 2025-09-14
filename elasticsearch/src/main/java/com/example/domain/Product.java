package com.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
//@Document(indexName = "products") // 指定索引名称
public class Product {
//    @Id
    private String id;

//    @Field(type = FieldType.Text)
    private String name;

//    @Field(type = FieldType.Double)
    private Double price;

//    @Field(type = FieldType.Keyword)
    private String category;

//    @Field(type = FieldType.Date)
    @JsonProperty("created_at")
    private String createdAt;
}
