package com.example.service;

/**
 * @Author: Niu
 * @Date: 2025/5/1 14:14
 * @Description: 生产者接口
 */
public interface RabbitProducerService {

    /**
     * 发送消息
     * @param exchange 交换机
     * @param queue 队列
     * @param message 消息
     */
    void sendMessage(String exchange,String queue,String message);

    /**
     * 发送延迟消息
     * @param exchange 交换机
     * @param queue 队列
     * @param message 消息
     * @param delayTime 延迟时间
     */
    void sendDelayMessage(String exchange,String queue,String message,long delayTime);
}
