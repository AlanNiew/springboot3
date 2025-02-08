package com.example.config;

import org.springframework.context.annotation.Configuration;

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
}
