package com.example.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author SouthWind
 * 编程千万条，规范第一条
 * Date 2025/2/6 22:32
 */
@Configuration
public class RabbitMqConfig {

//    @Bean
//    public Queue myQueue() {
//        return new Queue("myQueue", false); // 队列名称为 myQueue，非持久化
//    }

//    @Bean
//    public Queue workQueue() {
//        return new Queue("work.queue", false); // 队列名称为 myQueue，非持久化
//    }

/*    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2); // 最小并发消费者
        factory.setMaxConcurrentConsumers(10); // 最大并发消费者
        factory.setPrefetchCount(1); // 每个消费者每次只预取一条消息
        return factory;
    }*/

//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}
