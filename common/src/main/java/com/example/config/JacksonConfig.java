package com.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfig {

    /**
     * 自定义 ObjectMapper（全局生效）
     */
    @Bean
    @Primary  // 标记为默认的 ObjectMapper
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 日期序列化为字符串
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 空对象不报错
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略null
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 日期格式化
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 支持java8日期格式化
        mapper.registerModule(new JavaTimeModule());
        // 禁用默认的时间戳格式
        mapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        // 禁用反序列化时，未知属性时不报错（兼容老版本字段）
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 解决Long类型精度丢失的问题
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(simpleModule);
        return mapper;
    }
}