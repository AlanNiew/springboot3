package com.example.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: Niu
 * @Date: 2025/4/25 11:17
 * @Description:
 */
@Component
public class RabbitMqConfirmCallback implements RabbitTemplate.ConfirmCallback {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqConfirmCallback(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            System.err.println("消息未到达交换机, 原因: " + cause +
                    ", ID: " + (correlationData != null ? correlationData.getId() : "null"));
        }
    }
}
