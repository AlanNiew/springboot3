package com.example.service;

import cn.hutool.core.thread.ThreadUtil;
import com.example.config.RabbitMqConfig;
import com.example.entity.MyMsgObject;
import com.example.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class RabbitMQConsumer {

    private final JsonUtils jsonUtils;

    public RabbitMQConsumer(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    // 监听队列

    /*
    1. 简单模式（Simple Mode）交换器类型: 无（使用默认交换器）。
        todo routingKey 的作用:默认交换器（""）会将消息路由到与 routingKey 同名的队列。
        routingKey 必须与队列名称完全一致。
     */
    @RabbitListener(queuesToDeclare = @Queue(value = RabbitMqConfig.SIMPLE_QUEUE))
    public void receiveMessage(String message) {
        System.out.println("Received simple.queue message: " + message);
    }

    /*
    交换器类型: 无（使用默认交换器）。
    routingKey 的作用:与简单模式相同，routingKey 必须与队列名称完全一致。
    todo 多个消费者共享同一个队列中的消息
     */
    @RabbitListener(id = "simpleListener",
//            queuesToDeclare = @Queue(value = "work.queue",
//                durable = "true",
//                arguments = {
//                        @Argument(name = "x-dead-letter-exchange", value = "dlx.exchange"), // 死信交换器
//                        @Argument(name = "x-dead-letter-routing-key", value = "dlx.work") // 死信路由key
//                        }
//                ),
            queues = "work.queue",
            containerFactory = "simpleListenerFactory"
    )
    public void simpleReceiveMessage(@Payload Message msg,
                                     Channel channel) throws IOException {
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        MyMsgObject<String> msgObject = jsonUtils.fromJson(new String(msg.getBody()), new TypeReference<>() {});
        try {
            Thread.sleep(100);
            if (LocalTime.now().getSecond()%10 == 0){
                throw new RuntimeException("手动抛出异常");
            }
            System.out.println("simpleListenerFactory Received message: " + msgObject.getMessage());
            // 手动确认消息已处理
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            log.error("simpleListenerFactory Failed to process message: {},消息内容：{}", e.getMessage(),msgObject.getMessage());
            //如果处理失败，可以拒绝消息，消息将重新进入队列
//            channel.basicNack(deliveryTag,false,true);
            //如果处理失败，可以拒绝消息，消息将进入死信队列
            channel.basicNack(deliveryTag,false,false);
        }
    }

    @RabbitListener(id = "plusListener",queues = "work.queue",
            containerFactory = "plusListenerFactory"
    )
    public void plusReceiveMessage(@Payload List<Message> messages,
                                   Channel channel) throws IOException {
        long deliveryTag = messages.get(messages.size() - 1).getMessageProperties().getDeliveryTag();
        try {
            ThreadUtil.sleep(100);
            for (Message message : messages) {
                MyMsgObject<String> stringMyMsgObject = jsonUtils.fromJson(new String(message.getBody()), new TypeReference<MyMsgObject<String>>() {});
                System.out.println("plusListenerFactory Received message: " + stringMyMsgObject);
            }
            channel.basicAck(deliveryTag,true);
        } catch (Exception e) {
            log.error("plusListenerFactory Failed to process message: {},消息内容：{}", e.getMessage(),messages);
            channel.basicNack(deliveryTag,true,true);
        }
    }

/*    @RabbitListener(queuesToDeclare = @Queue(value = "work.queue"),
            concurrency = "2-5",
            messageConverter = "jsonMessageConverter")
    public void receiveMessage2(Map<String, Object> message) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Received Map: " + message);
    }*/

    /*
    发布/订阅模式（Publish/Subscribe Mode）
      交换器类型: fanout
        routingKey 的作用:
       todo:: fanout 交换器会忽略 routingKey，将消息广播到所有绑定的队列。 routingKey 可以设置为空或任意值
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue1", durable = "true"),
            exchange = @Exchange(name = "jobs.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void listenerFanoutQueue1(String msg) {
        System.out.println("接收到fanout.queue【1】消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "fanout.queue2",durable = "true"),
            exchange = @Exchange(name = "jobs.fanout", type = ExchangeTypes.FANOUT)
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
            value = @Queue(name = "direct.queue1", durable = "true"),
            exchange = @Exchange(name = "jobs.direct"),
            key = "error"
    ))
    public void listenerDirectQueue1(String msg) {
        System.out.println("接收到error消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "jobs.direct"),
            key = "warn"
    ))
    public void listenerDirectQueue2(String msg) {
        System.out.println("接收到warn消息：" + msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue3", durable = "true"),
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
            value = @Queue(name = "topic.queue1", durable = "true"),
            exchange = @Exchange(name = "jobs.topic", type = ExchangeTypes.TOPIC),
            key = {"user.*", "auth.#"}
    ))
    public void listenerTopicQueue1(String msg) {
        System.out.println("接收到user消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2", durable = "true"),
            exchange = @Exchange(name = "jobs.topic", type = ExchangeTypes.TOPIC),
            key = {"order.#"}
    ))
    public void listenerTopicQueue2(String msg) {
        System.out.println("接收到order消息：" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue3", durable = "true"),
            exchange = @Exchange(name = "jobs.topic", type = ExchangeTypes.TOPIC),
            key = {"#.news"}
    ))
    public void listenerTopicQueue3(String msg) {
        System.out.println("接收到news消息：" + msg);
    }

    //延迟消息消费者
    @RabbitListener(queues = "delayed.queue", containerFactory = "simpleListenerFactory")
    public void receiveDelayMessage(Message message,Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        MyMsgObject stringMyMsgObject = jsonUtils.fromJson(new String(message.getBody()), MyMsgObject.class);
        try {
            ThreadUtil.sleep(100);
            if (LocalTime.now().getSecond()%2==0){
                throw new RuntimeException("测试异常");
            }
            System.out.println("Delayed Queue Received delayed message: " + stringMyMsgObject.getMessage());
            channel.basicAck(deliveryTag,false);
        }catch (Exception e){
            log.error("receiveDelayMessage Failed to process message: {},消息内容：{}", e.getMessage(),stringMyMsgObject.getMessage());
            channel.basicReject(deliveryTag,false);
        }
    }
    //死信队列消费者
    @RabbitListener(queues = "dlx.queue", containerFactory = "simpleListenerFactory")
    public void receiveDlxMessage(Message message,  Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            MyMsgObject<String> stringMyMsgObject = jsonUtils.fromJson(new String(message.getBody()), new TypeReference<>() {});
            System.out.println("Dlx Queue Received delayed message: " + stringMyMsgObject.getMessage());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            log.error("receiveDlxMessage Failed to process message: {},消息内容：{}", e.getMessage(),message);
            channel.basicReject(deliveryTag,false);
        }
    }
}