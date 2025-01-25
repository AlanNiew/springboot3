package com.example.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationGlobalFilter implements GlobalFilter {

    private static final String JWT_SECRET_KEY = "your_secret_key";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从请求头中获取 Authorization 字段中的 Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        // 如果没有 Token，则返回未授权错误
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        // 截取 Bearer 后的 token 部分
        token = token.substring(7);
        
        try {
            // 解析 Token
//            Claims claims = Jwts.parser()
//                    .setSigningKey(JWT_SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            // 提取角色或其他用户信息（如用户名）
//            String username = claims.getSubject();
//            String role = claims.get("role", String.class);
//
//            // 在 SecurityContext 中设置认证信息（假设已经有自定义的认证逻辑）
//            Authentication authentication = new JwtAuthentication(username, role);
//            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 继续执行过滤器链
            return chain.filter(exchange);

        } catch (Exception e) {
            // 如果 Token 无效，返回未授权错误
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
