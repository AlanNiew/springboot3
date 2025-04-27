package com.example.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SouthWind
 * 编程千万条，规范第一条
 * Date 2025/2/6 22:32
 */
@Configuration
public class RabbitMqConfig {

//    @Bean
//    public Queue workQueue() {
//        return new Queue("work.queue", false); // 队列名称为 myQueue，非持久化
//    }

    /**
     * 单并发消费
     * @param connectionFactory 连接工厂
     * @param simpleMessageConverter 消息转换器
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleListenerFactory(
            ConnectionFactory connectionFactory,
            SimpleMessageConverter simpleMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(simpleMessageConverter); // 设置消息转换器
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置手动确认模式
        factory.setConcurrentConsumers(1); // 最小并发消费者
        factory.setMaxConcurrentConsumers(2); // 最大并发消费者
        factory.setPrefetchCount(1); // 每个消费者每次只预取一条消息
        return factory;
    }

    /**
     * 并发消费
     * @param connectionFactory 连接工厂
     * @param jsonMessageConverter 消息转换器
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory plusListenerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter); // 设置消息转换器
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置手动确认模式
        factory.setBatchSize(10);
        factory.setBatchListener(true); // 设置批量监听
        factory.setConsumerBatchEnabled(true); // 设置批量消费
//        factory.setBatchingStrategy(new SimpleBatchingStrategy(10, 100, 1000));
        factory.setConcurrentConsumers(1); // 最小并发消费者
        factory.setMaxConcurrentConsumers(5); // 最大并发消费者
        factory.setPrefetchCount(20); // 每个消费者每次只预取一条消息
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // 用于转换 JSON 类型消息
    }

    @Bean
    public SimpleMessageConverter simpleMessageConverter() {
        return new SimpleMessageConverter(); // 用于转换 String 类型消息
    }
}
