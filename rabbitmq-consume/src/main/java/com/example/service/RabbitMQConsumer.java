package com.example.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    // 监听队列
    @RabbitListener(queues = "myQueue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false); // 队列名称为 myQueue，非持久化
    }
}