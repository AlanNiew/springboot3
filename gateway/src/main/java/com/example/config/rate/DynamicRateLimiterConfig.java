package com.example.config.rate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

//@RefreshScope //todo 走nacos 热更新配置更简单
//@Configuration
public class DynamicRateLimiterConfig {

    @Value("${gateway.rate-limiter.replenishRate:10}")
    private int replenishRate;

    @Value("${gateway.rate-limiter.burstCapacity:20}")
    private int burstCapacity;

    @Bean("customRedisRateLimiter")
    @Primary // 设置为默认选择
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(replenishRate, burstCapacity);
    }
}
