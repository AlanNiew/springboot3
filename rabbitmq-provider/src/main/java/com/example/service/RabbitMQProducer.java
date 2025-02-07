package com.example.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate, Queue simpleQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = simpleQueue;
    }

    // 发送消息
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println("Sent message: " + message);
    }

    public void sendMessage(String queue,String message) {
        rabbitTemplate.convertAndSend(queue, message);
        System.out.println("Sent message: " + message);
    }

}