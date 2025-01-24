package com.example.service;

import com.example.entity.UserDO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Author:Niu
 * Date:2025/1/19 16:12
 * Description: nothing.
 */
public interface UserService {

    UserDO getUserById(Long id);

    List<UserDO> getUserList();

    Page<UserDO> pageList(Integer pageNum, Integer pageSize);
}
