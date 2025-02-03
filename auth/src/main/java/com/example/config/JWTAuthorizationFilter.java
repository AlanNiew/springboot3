package com.example.config;

import com.example.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String SECRET_KEY = "yourSecretKey";  // 你可以根据实际情况修改秘钥

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // 提取 JWT（去除 "Bearer " 部分）

            try {
                // 解析 JWT 并获取用户名
                String username = JwtTokenUtil.getUsernameFromToken(token);

                if (username != null) {
                    // 创建认证对象并存入 SecurityContext
                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 处理 token 无效或过期的情况
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);  // 继续过滤器链
    }
}
