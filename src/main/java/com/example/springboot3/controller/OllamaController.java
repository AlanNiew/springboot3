package com.example.springboot3.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author:Niu
 * Date:2025/1/4 15:23
 */
@RestController
public class OllamaController {
    @Resource
    private OllamaChatModel model;
    private final ChatMemory chatMemory = new InMemoryChatMemory();
    //个人单独聊天 client
    Map<String,ChatClient> chatClientMap = new ConcurrentHashMap<>();
    public ChatClient creatClient(String userId) {
        System.out.println("初始化,创建client");
        return ChatClient.builder(model)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory, userId,100))
                .build();
    }
    @GetMapping("/chat")
    public Object chat(@RequestParam String msg, HttpServletRequest request) {
        String userId = request.getSession().getId();
        System.out.println("userId:"+userId);
        ChatClient client = chatClientMap.computeIfAbsent(userId,this::creatClient);
        return client.prompt(msg).call().content();
    }
}
