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
    //默认交换机
    public static final String SIMPLE_QUEUE = "simple.queue";
    public static final String WORK_QUEUE = "work.queue";
    //直连
    public static final String DIRECT_EXCHANGE = "jobs.direct"; // 直连交换机
    public static final String DIRECT_QUEUE_1 = "direct.queue1"; // 直连队列1
    public static final String DIRECT_QUEUE_2 = "direct.queue2"; // 直连队列2
    public static final String DIRECT_QUEUE_3 = "direct.queue3"; // 直连队列3
    // 主题
    public static final String TOPIC_EXCHANGE = "jobs.topic"; // 主题交换机
    public static final String TOPIC_QUEUE_1 = "topic.queue1"; // 主题队列1
    public static final String TOPIC_QUEUE_2 = "topic.queue2"; // 主题队列2
    public static final String TOPIC_QUEUE_3 = "topic.queue3"; // 主题队列3
    // 广播
    public static final String FANOUT_EXCHANGE = "jobs.fanout"; // 广播交换机
    public static final String FANOUT_QUEUE_1 = "fanout.queue1"; // 广播队列1
    public static final String FANOUT_QUEUE_2 = "fanout.queue2"; // 广播队列2
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
        return new Queue(WORK_QUEUE,
                true,
                false,
                false,
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
            return QueueBuilder.durable(DLX_QUEUE)
                    .withArgument("x-message-ttl", 1000*60*60) // 消息过期时间，1小时
                    .withArgument("x-max-length", 10_000) //队列最大长度，最大1万条
                    .withArgument("x-overflow", "reject-publish") // 队列达到最大长度后，新消息的拒绝策略
                    .build();
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
