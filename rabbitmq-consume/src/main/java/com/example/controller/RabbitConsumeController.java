package com.example.controller;

import com.example.api.CommonResult;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

/**
 * @Author: Niu
 * @Date: 2025/4/30 15:29
 * @Description:
 */
@Controller
@ResponseBody
@RequestMapping("/rabbit/consume")
public class RabbitConsumeController {

    @Resource
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    @RequestMapping(value = "/start/{name}",method = RequestMethod.GET)
    public CommonResult<?> start(@PathVariable("name") String name){
        MessageListenerContainer listenerContainer = rabbitListenerEndpointRegistry.getListenerContainer(name);
        if (Objects.nonNull(listenerContainer) && !listenerContainer.isRunning()) {
            listenerContainer.start();
        }
        return CommonResult.success("start"+name);
    }

    @RequestMapping(value = "/stop/{name}",method = RequestMethod.GET)
    public CommonResult<?> stop(@PathVariable("name") String name){
        MessageListenerContainer listenerContainer = rabbitListenerEndpointRegistry.getListenerContainer(name);
        if (Objects.nonNull(listenerContainer) && listenerContainer.isRunning()) {
            listenerContainer.stop();
        }
        return CommonResult.success("stop"+name);
    }

    @PostConstruct
    public void checkListeners() {
        System.out.println("Registered listener IDs: " +
                rabbitListenerEndpointRegistry.getListenerContainerIds()); // 打印所有ID
    }
}
