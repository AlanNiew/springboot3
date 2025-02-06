package com.example.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.example.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationGlobalFilter implements GlobalFilter, Ordered {


    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * TODO :: 1.先看用户是否拥有认证信息，2.再看用户是否拥有对应的权限
     */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /*
         * 1.先看用户是否拥有认证信息
         */
        //登录请求不需要认证
        if(ignoreRequest(exchange)){
            return chain.filter(exchange);
        }
        // 从请求头中获取 Authorization 字段中的 Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        // 如果没有 Token，则返回未授权错误
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 截取 Bearer 后的 token 部分
        token = token.substring(7);
        String subject;//用户名
        try {
            // 解析 Token
            Claims claims = JwtTokenUtil.parseToken(token);
            subject = claims.getSubject();

            //验证Token
            boolean validateToken = JwtTokenUtil.validateToken(token, subject);
            if(!validateToken){
                return this.writeErrorResponse(exchange, 401, "Token is invalid");
            }

            /*
             * 2.看用户是否拥有对应的权限或者角色
             */

            //获取用户角色和权限, todo 这里需要从数据库中获取用户角色和权限
            List<String> roles = claims.get("roles", List.class);
            List<String> permissions = claims.get("permissions", List.class);
            //判断用户是否有权限
            if(!hasPermission(exchange.getRequest().getURI().getPath(), roles, permissions)){
                return this.writeErrorResponse(exchange, 403, "权限不足");
            }

        } catch (Exception e) {
            // 如果 Token 无效，返回未授权错误
            return this.writeErrorResponse(exchange, 403, "认证错误");
        }
        // 将解析后的用户信息添加到请求头中
        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-User", subject)
                .build();
        exchange = exchange.mutate().request(modifiedRequest).build();
        // 继续执行过滤器链
        return chain.filter(exchange);
    }

    //忽略部分请求地址
    private boolean ignoreRequest(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        return path.startsWith("/auth/login") || path.startsWith("/auth/register");
    }

    // 使用 AntPathMatcher 进行路径权限匹配
    private boolean hasPermission(String path, List<String> roles, List<String> permissions) {
        for (String role : roles) {
            if (pathMatcher.match(role, path)) {
                return true;
            }
        }
        for (String permission : permissions) {
            if (pathMatcher.match(permission, path)) {
                return true;
            }
        }
        return false;
    }


    // 返回 错误信息
    private Mono<Void> writeErrorResponse(ServerWebExchange exchange,int code, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        String body = "{\"code\":" + code + ", \"message\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }



    @Override
    public int getOrder() {
        return -1;
    }
}
