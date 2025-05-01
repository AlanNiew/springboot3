package com.example.service.impl;

import com.example.entity.MyMsgObject;
import com.example.service.RabbitProducerService;
import com.example.utils.JsonUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RabbitMQProducerServiceImpl implements RabbitProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final JsonUtils jsonUtils;

    @Autowired
    public RabbitMQProducerServiceImpl(RabbitTemplate rabbitTemplate, JsonUtils jsonUtils) {
        this.rabbitTemplate = rabbitTemplate;
        this.jsonUtils = jsonUtils;
//        this.queue = simpleQueue;
    }

    // 发送消息
    public void sendMessage(String exchange,String queue,String message) {
        MessageProperties messageProperties = new MessageProperties();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int anInt = random.nextInt(100, 200);
        for (int i = 0; i < anInt; i++) {
            sendMessage(exchange, queue, message+"_"+i,messageProperties);
        }
        System.out.println("Sent message: " + message+",共"+anInt+"条。");
    }

    //发送延迟消息
    public void sendDelayMessage(String exchange,String queue,String message,long delayTime) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("x-delay", delayTime);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int anInt = random.nextInt(1, 5);
        for (int i = 0; i < anInt; i++) {
            String msg = "延迟消息_" + message + "_" + i;
            sendMessage(exchange, queue, msg, messageProperties);
        }
        System.out.println("Sent delay message: " + message+",共"+anInt+"条。");
    }

    private void sendMessage(String exchange,String queue,String msg,MessageProperties messageProperties) {
        CorrelationData correlationData = new CorrelationData();
        if (messageProperties == null){
            messageProperties = new MessageProperties();
        }
//        messageProperties.setContentType("application/json");
        Message message = new Message(jsonUtils.toJson(new MyMsgObject<>(msg, correlationData.getId()))
                .getBytes(StandardCharsets.UTF_8), messageProperties);
        rabbitTemplate.convertAndSend(exchange, queue, message, correlationData);
    }

}