package com.example.service;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    // 监听队列

    /* SimpleQueue 接收消息代码 */
    @RabbitListener(queuesToDeclare = @Queue(value = "myQueue",declare = "false"))
    public void receiveMessage(String message) {
        System.out.println("Received myQueue message: " + message);
    }

    /* WorkQueue 接收消息代码，这里写了 2 个监听程序，用以模拟 2 个消费者 */
    @RabbitListener(queuesToDeclare = @Queue(value = "work.queue",declare = "false"))
    public void receiveMessage1(String message) {
        System.out.println("Received work.queue message1: " + message);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "work.queue",declare = "false"))
    public void receiveMessage2(String message) {
        System.out.println("Received work.queue message2: " + message);
    }

}