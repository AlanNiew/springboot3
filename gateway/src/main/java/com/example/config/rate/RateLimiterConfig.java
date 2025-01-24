package com.example.config.rate;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Configuration
public class RateLimiterConfig {

    // 基于客户端 IP 的 Key 解析器
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
            if (remoteAddress != null) {
                String hostAddress = remoteAddress.getAddress().getHostAddress();
                return Mono.just(hostAddress);
            }else {
                return Mono.just("unknown");
            }
        };
    }
}
