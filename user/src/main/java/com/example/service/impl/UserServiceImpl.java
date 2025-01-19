package com.example.service.impl;

import com.example.dao.UserRepository;
import com.example.entity.UserDO;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:Niu
 * Date:2025/1/19 16:13
 * Description: nothing.
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Override
    public UserDO getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<UserDO> getUserList() {
        return userRepository.findAll();
    }
}
