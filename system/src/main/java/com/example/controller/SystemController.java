package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author:Niu
 * Date:2025/1/19 16:08
 * Description: nothing.
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

}
