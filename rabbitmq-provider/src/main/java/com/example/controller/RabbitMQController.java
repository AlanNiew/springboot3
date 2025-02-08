package com.example.controller;

import com.example.service.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQController {

    @Autowired
    private RabbitMQProducer producer;

    // 发送消息的接口
    @GetMapping("/send")
    public String sendMessage1(
            @RequestParam(defaultValue = "") String exchange,
            @RequestParam(defaultValue = "simple.queue") String queue,@RequestParam String message) {
        producer.sendMessage(exchange,queue,message);
        return "Message sent: " + message;
    }
}