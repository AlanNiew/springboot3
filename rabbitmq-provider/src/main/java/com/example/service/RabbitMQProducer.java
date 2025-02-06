package com.example.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    // 发送消息
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(queue.getName(), message);
        System.out.println("Sent message: " + message);
    }

}