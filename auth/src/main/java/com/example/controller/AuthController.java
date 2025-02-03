package com.example.controller;

import com.example.api.CommonResult;
import com.example.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
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

    @GetMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.success("auth test");
    }

    @PostMapping("/login")
    public CommonResult<?> login(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return CommonResult.fail("用户名或密码不能为空");
        }
        //模拟从数据库中取得用户信息登录
        if (username.equals("admin") && password.equals("admin")) {
            Map<String, Object> jwt = JwtTokenUtil.buildJwt(username);
            return CommonResult.success(jwt);
        }else{
            return CommonResult.fail("用户名或密码错误");
        }
    }
}
