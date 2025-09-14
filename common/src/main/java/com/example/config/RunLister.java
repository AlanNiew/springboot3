package com.example.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author: Niu
 * @Date: 2025/4/25 08:22
 * @Description:
 */
@Configuration
@Order(1)
public class RunLister implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("项目启动成功！");
    }
}
