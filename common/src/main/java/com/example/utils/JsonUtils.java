package com.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JsonUtils {

    private final ObjectMapper objectMapper;

    @Autowired
    public JsonUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 对象转 JSON 字符串
     */
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    /**
     * JSON 字符串转对象（支持泛型）
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }

    /**
     * JSON 字符串转复杂对象（如带泛型的 List/Map）
     */
    public <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
}