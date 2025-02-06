package com.example.config;

import com.example.service.AuthService;
import com.example.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authorization 授权 过滤器
 */
@Slf4j
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    // 你可以根据实际情况修改秘钥

    private final AuthService authService;

    public JWTAuthorizationFilter(AuthService authService) {
        this.authService = authService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/auth/login")){
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // 提取 JWT（去除 "Bearer " 部分）

            try {
                // 解析 JWT 并获取用户名
                String username = JwtTokenUtil.getUsernameFromToken(token);
                UserDetails user = authService.loadUserByUsername(username);
                //手动组装一个认证对象
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities()); //已经认证过了，密码这里可以直接为空
                // 将认证对象放到上下文中
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception e) {
                // 处理 token 无效或过期的情况
                SecurityContextHolder.clearContext();
                log.error("Invalid JWT token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);  // 继续过滤器链
    }
}
