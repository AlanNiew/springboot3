package com.example.controller;

import com.example.service.RabbitProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQController {

    @Autowired
    private RabbitProducerService producer;

    // 发送消息的接口
    @GetMapping("/send")
    public String sendMessage1(
            @RequestParam(defaultValue = "") String exchange,
            @RequestParam(defaultValue = "simple.queue") String queue,
            @RequestParam String message) {
        this.producer.sendMessage(exchange,queue,message);
        return "Message sent: " + message;
    }

    @GetMapping("/sendDelay")
    public String sendMessage2(
            @RequestParam(defaultValue = "delayed.exchange") String exchange,
            @RequestParam(defaultValue = "delayed.routeKey") String queue,
            @RequestParam String message,
            @RequestParam(defaultValue = "5000") long delayTime) {
        this.producer.sendDelayMessage(exchange,queue,message,delayTime);
        return "Message sent: " + message;
    }
}