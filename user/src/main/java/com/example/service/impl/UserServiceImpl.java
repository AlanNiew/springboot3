package com.example.service.impl;

import com.example.dao.UserRepository;
import com.example.entity.UserDO;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<UserDO> pageList(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
        return userRepository.findAll(pageRequest);
    }

    @Override
    public UserDO getUserByName(String name) {
        Optional<UserDO> optional = userRepository.findByName(name);
        return optional.orElse(null);
    }
}
