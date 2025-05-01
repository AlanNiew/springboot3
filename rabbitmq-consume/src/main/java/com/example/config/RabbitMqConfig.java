package com.example.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SouthWind
 * 编程千万条，规范第一条
 * Date 2025/2/6 22:32
 */
@Configuration
public class RabbitMqConfig {

    public static final String SIMPLE_QUEUE = "simple.queue";
    public static final String WORK_QUEUE = "work.queue";
    public static final String DIRECT_QUEUE = "direct.queue";
    public static final String TOPIC_QUEUE = "topic.queue";
    public static final String FANOUT_QUEUE = "fanout.queue";
    //死信
    public static final String DLX_EXCHANGE = "dlx.exchange"; // 死信交换机
    public static final String DLX_QUEUE = "dlx.queue";  //死信队列
    public static final String DLX_ROUTING_KEY = "dlx.work"; // 死信路由键
    //延时
    public static final String DELAY_EXCHANGE = "delayed.exchange"; // 延时消息交换机
    public static final String DELAY_QUEUE = "delayed.queue"; // 延时消息队列
    public static final String DELAY_ROUTING_KEY = "delayed.routeKey"; // 延时路由键

    @Bean
    public Queue workQueue() {
        return new Queue("work.queue", true,false,false,
                Map.of(
                        "x-dead-letter-exchange", DLX_EXCHANGE,
                        "x-dead-letter-routing-key", DLX_ROUTING_KEY
                )); // 队列名称为 myQueue，非持久化
    }

        // 死信交换机
        @Bean
        public DirectExchange dlxExchange() {
            return new DirectExchange(DLX_EXCHANGE, true ,false) ;
        }
        // 死信队列
        @Bean
        public Queue dlq() {
            return new Queue(DLX_QUEUE, true);
        }
        // 绑定死信交换机和队列
        @Bean
        public Binding dlqBinding() {
            return BindingBuilder.bind(dlq())
                    .to(dlxExchange())
                    .with(DLX_ROUTING_KEY);
        }
        // 延时交换机
        @Bean
        public CustomExchange delayedExchange() {
            Map<String, Object> args = new HashMap<>();
            args.put("x-delayed-type", "direct"); // 最终路由方式
//            args.put("x-delay", 5000); //x-delay 是每条消息的延迟时间，不是交换机的配置项,它应当由生产者发送消息时通过 MessageProperties 单独设置。
            return new CustomExchange(
                    DELAY_EXCHANGE,
                    "x-delayed-message", // 固定类型
                    true,
                    false,
                    args
            );
        }
        // 延迟队列
        @Bean
        public Queue delayedQueue() {
            return QueueBuilder.durable(DELAY_QUEUE)
                    .withArgument("x-dead-letter-exchange", "dlx.exchange") // 死信交换机
                    .withArgument("x-dead-letter-routing-key", "dlx.work")  // 死信路由键
                    .build();
        }

        // 绑定延迟交换机和队列
        @Bean
        public Binding bindingDelayed() {
            return BindingBuilder.bind(delayedQueue())
                    .to(delayedExchange())
                    .with(DELAY_ROUTING_KEY)
                    .noargs();
        }
    /**
     * 单并发消费
     * @param connectionFactory 连接工厂
     * @param simpleMessageConverter 消息转换器
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleListenerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter simpleMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(simpleMessageConverter); // 设置消息转换器
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置手动确认模式
//        factory.setConcurrentConsumers(1); // 最小并发消费者
//        factory.setMaxConcurrentConsumers(2); // 最大并发消费者
        factory.setPrefetchCount(1); // 每个消费者每次只预取一条消息
        factory.setAutoStartup(true); // 设置自动启动
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
//        factory.setConcurrentConsumers(1); // 最小并发消费者
//        factory.setMaxConcurrentConsumers(5); // 最大并发消费者
        factory.setPrefetchCount(20); // 每个消费者每次只预取一条消息
        factory.setAutoStartup(false); // 设置自动启动
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
