package com.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author SouthWind
 * 编程千万条，规范第一条
 * Date 2025/2/6 22:32
 */
@Configuration
public class RabbitMqConfig {

/*    @Bean
    public Queue simpleQueue() {
        return new Queue("simple.queue", false); // 队列名称为 myQueue，非持久化
    }

    @Bean
    public Queue workQueue() {
        return new Queue("work.queue", false); // 队列名称为 myQueue，非持久化
    }*/
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 设置消息转换器为 JSON（Jackson）
        rabbitTemplate.setMessageConverter(jsonMessageConverter);

        return rabbitTemplate;
    }
    /**
     *
     模式	交换器类型	routingKey 的作用
     简单模式	无（默认交换器）	必须与队列名称完全一致。
     工作队列模式	无（默认交换器）	必须与队列名称完全一致。
     发布/订阅模式	fanout	被忽略，消息广播到所有绑定的队列。
     路由模式	direct	必须与队列绑定的 routingKey 完全匹配。
     主题模式	topic	支持通配符匹配（* 和 #）。
     头部交换模式	headers	被忽略，路由基于消息头部信息。
     RPC 模式	无（默认交换器）	必须与队列名称完全一致。
     死信队列模式	无（默认交换器）	必须与队列名称完全一致。
     延迟队列模式	direct/topic	与路由模式或主题模式相同。
     优先级队列模式	无（默认交换器）	必须与队列名称完全一致。
     *
     */

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
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
        return new Jackson2JsonMessageConverter(mapper);
    }
}
