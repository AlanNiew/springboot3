package com.example.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.api.CommonResult;
import com.example.client.UserClient;
import com.example.entity.UserDO;
import com.example.entity.UserDetail;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Niu
 * Date:2025/2/5 15:27
 * Description: nothing.
 */
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    @Autowired
    private UserClient userClient;
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 从数据库中查询出用户实体对象
        CommonResult<?> result = userClient.getUserByName(username);
        UserDO userDO = Convert.convert(UserDO.class, result.getData());
        if (userDO == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 查询用户权限，并授予
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (username.equals("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ALL"));
        }
        return new UserDetail(userDO, authorities);
    }
}
