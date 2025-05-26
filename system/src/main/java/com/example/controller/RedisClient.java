package com.example.controller;

import com.example.api.CommonResult;
import com.example.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Niu
 * @Date: 2025/5/20 15:53
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisClient {

    private final RedisUtil redisUtil;

    private static final String LOCK_PREFIX = "LOCK:";

    @RequestMapping("/test")
    public CommonResult<?> test() {
        List<String> keysByPrefix = redisUtil.getKeysByPrefix(LOCK_PREFIX);
        return CommonResult.success(keysByPrefix);
    }
}
