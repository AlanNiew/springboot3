package com.example.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

//    private final Queue queue;
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
//        this.queue = simpleQueue;
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
            rabbitTemplate.convertAndSend(exchange, queue ,i+":"+message);
        }
        System.out.println("Sent message: " + message);
    }

}