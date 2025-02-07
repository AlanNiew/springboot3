package com.example.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SouthWind
 * 编程千万条，规范第一条
 * Date 2025/2/6 22:32
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue simpleQueue() {
        return new Queue("myQueue", false); // 队列名称为 myQueue，非持久化
    }

    @Bean
    public Queue workQueue() {
        return new Queue("work.queue", false); // 队列名称为 myQueue，非持久化
    }
}
