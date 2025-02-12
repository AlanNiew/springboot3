package com.example.service;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class RabbitMQConsumer {

    // 监听队列

    /*
    1. 简单模式（Simple Mode）交换器类型: 无（使用默认交换器）。
        todo routingKey 的作用:默认交换器（""）会将消息路由到与 routingKey 同名的队列。
        routingKey 必须与队列名称完全一致。
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "simple.queue"))
    public void receiveMessage(String message) {
        System.out.println("Received myQueue message: " + message);
    }

    /*
    交换器类型: 无（使用默认交换器）。
    routingKey 的作用:与简单模式相同，routingKey 必须与队列名称完全一致。
    todo 多个消费者共享同一个队列中的消息
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "work.queue",durable = "false"),
            concurrency = "2-10",
            messageConverter = "simpleMessageConverter",
            ackMode = "MANUAL"
    )
    public void receiveMessage1(String msg, Channel channel,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        try {
            Thread.sleep(100);
            if (random.nextInt(1,10)%2==0){
                throw new Exception("手动抛出异常");
            }
            System.out.println("Received work.queue message1: " + msg);
            // 手动确认消息已处理
            channel.basicAck(tag,false);
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
            //如果处理失败，可以拒绝消息，消息将重新进入队列
            channel.basicNack(tag,false,true);
        }

    }

    @RabbitListener(queues = "work.queue",concurrency = "1-2",messageConverter = "jsonMessageConverter")
    public void receiveMessage2(Map<String, Object> message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Received Map: " + message);
    }

    /*
    发布/订阅模式（Publish/Subscribe Mode）
      交换器类型: fanout
        routingKey 的作用:
       todo:: fanout 交换器会忽略 routingKey，将消息广播到所有绑定的队列。 routingKey 可以设置为空或任意值
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue1", durable = "false"),
            exchange = @Exchange(name = "jobs.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void listenerFanoutQueue1(String msg) {
        System.out.println("接收到fanout.queue【1】消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue2",durable = "false"),
            exchange = @Exchange(name = "jobs.fanout", type = ExchangeTypes.FANOUT,delayed = "true")
    ))
    public void listenerFanoutQueue2(String msg) {
        System.out.println("接收到fanout.queue【2】消息：" + msg);
    }


    /*
    路由模式（Routing Mode）
      交换器类型: direct
        routingKey 的作用:
          todo:: direct 交换器会根据 routingKey 将消息路由到与之完全匹配的队列。 队列绑定到交换器时需要指定一个 routingKey。
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1", durable = "false"),
            exchange = @Exchange(name = "jobs.direct"),
            key = "error"
    ))
    public void listenerDirectQueue1(String msg) {
        System.out.println("接收到error消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "false"),
            exchange = @Exchange(name = "jobs.direct"),
            key = "warn"
    ))
    public void listenerDirectQueue2(String msg) {
        System.out.println("接收到warn消息：" + msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue3", durable = "false"),
            exchange = @Exchange(name = "jobs.direct"),
            key = "info"
    ))
    public void listenerDirectQueue3(String msg) {
        System.out.println("接收到info消息：" + msg);
    }

    /*
    主题模式（Topic Mode）
      交换器类型: topic
        routingKey 的作用:
          todo:: topic 交换器支持通配符匹配。 routingKey 是一个由点号（.）分隔的字符串。
          队列绑定到交换器时可以使用通配符：
            * 匹配一个单词。
            # 匹配零个或多个单词。
            todo:: 旧的routeking 不会自动删除，需要手动去管理界面解除。
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1", durable = "false"),
            exchange = @Exchange(name = "jobs.topic", type = ExchangeTypes.TOPIC),
            key = {"user.*", "auth.#"}
    ))
    public void listenerTopicQueue1(String msg) {
        System.out.println("接收到user消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2", durable = "false"),
            exchange = @Exchange(name = "jobs.topic", type = ExchangeTypes.TOPIC),
            key = {"order.#"}
    ))
    public void listenerTopicQueue2(String msg) {
        System.out.println("接收到order消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue3", durable = "false"),
            exchange = @Exchange(name = "jobs.topic", type = ExchangeTypes.TOPIC),
            key = {"#.news"}
    ))
    public void listenerTopicQueue3(String msg) {
        System.out.println("接收到news消息：" + msg);
    }
}