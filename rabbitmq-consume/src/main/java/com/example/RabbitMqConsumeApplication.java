package com.example;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class RabbitMqConsumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMqConsumeApplication.class, args);
    }
}