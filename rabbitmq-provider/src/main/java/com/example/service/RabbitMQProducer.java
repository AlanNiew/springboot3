package com.example.service;

import com.example.entity.MyMsgObject;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    private final MessageProperties messageProperties = new MessageProperties();
//    private final Queue queue;
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
//        this.queue = simpleQueue;
        messageProperties.setContentType("application/json");
    }

    // 发送消息
    public void sendMessage(String message) {
//        rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println("Sent message: " + message);
    }

    public void sendMessage(String exchange,String queue,String message) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int anInt = random.nextInt(10, 100);
        for (int i = 0; i < anInt; i++) {
            CorrelationData correlationData = new CorrelationData();
            MyMsgObject<String> msgObject = new MyMsgObject<>(message+"_"+i,correlationData.getId());
            rabbitTemplate.convertAndSend(exchange, queue, msgObject, correlationData);
        }
        System.out.println("Sent message: " + message);
    }

}