package com.example.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Author:Niu
 * Date:2025/2/5 15:27
 * Description: nothing.
 */
public interface AuthService {

    UserDetails loadUserByUsername(String username);
}
