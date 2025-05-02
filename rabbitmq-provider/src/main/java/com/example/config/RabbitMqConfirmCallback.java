package com.example.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Niu
 * @Date: 2025/4/25 11:17
 * @Description:
 */
@Slf4j
@Component
public class RabbitMqConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    private final RabbitTemplate rabbitTemplate;

    private final ReentrantLock lock = new ReentrantLock();
    public RabbitMqConfirmCallback(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @PostConstruct
    public void init() {
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
         // 锁
        boolean b = lock.tryLock();
        try{
            if (!b){
                throw new RuntimeException("抢锁失败！");
            }
            if (!ack) {
                log.error("消息未到达交换机, 原因: " + cause +
                        ", ID: " + (correlationData != null ? correlationData.getId() : "null"));
            }
        }catch (Exception e){
            log.error("消息未到达交换机, 错误信息: " + e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息未到达队列, 原因: " + returned.getReplyText() +
                ", 路由键: " + returned.getRoutingKey() +
                ", 交换机: " + returned.getExchange() +
                ", 消息: " + new String(returned.getMessage().getBody()));
    }
}
