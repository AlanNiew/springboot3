package com.example.controller;

import com.example.api.CommonResult;
import com.example.client.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:Niu
 * Date:2025/1/19 16:08
 * Description: nothing.
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private UserClient userClient;
    @RequestMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @RequestMapping("/user")
    public CommonResult<?> user() {
        CommonResult<?> result = userClient.getAllUser();
        log.info("result:{}", result);
        return result;
    }
}
