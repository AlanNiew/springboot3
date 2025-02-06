package com.example.controller;

import cn.hutool.core.convert.Convert;
import com.example.api.CommonResult;
import com.example.client.UserClient;
import com.example.entity.UserDO;
import com.example.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Author:Niu
 * Date:2025/2/3 16:01
 * Description: nothing.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserClient userClient;

    @GetMapping("/test")
//    加权限
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<String> test() {
        return CommonResult.success("auth test");
    }

    @PostMapping("/login")
    public CommonResult<?> login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return CommonResult.fail("用户名或密码不能为空");
        }
        CommonResult<?> userByName = userClient.getUserByName(username);
        UserDO userDO = Convert.convert(UserDO.class, userByName.getData());
        //从数据库中取得用户信息登录
        boolean matches = passwordEncoder.matches(password, userDO.getPassword());
        if (username.equals("admin") && matches) {
            Map<String, Object> jwt = JwtTokenUtil.buildJwt(username);
            return CommonResult.success(jwt);
        }else{
            return CommonResult.fail("用户名或密码错误");
        }
    }
}
