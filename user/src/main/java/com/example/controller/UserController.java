package com.example.controller;

import com.example.api.CommonResult;
import com.example.entity.UserDO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author:Niu
 * Date:2025/1/19 17:06
 * Description: nothing.
 */
@RestController
@RequestMapping("/user")
@RefreshScope
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${user.age}")
    private Integer age;
    @GetMapping("/all")
    public CommonResult<?> test() {
        List<UserDO> userList = this.userService.getUserList();
        return CommonResult.success(userList, "查询成功");
    }

    @GetMapping("/byId/{id}")
    public CommonResult<?> test2(@PathVariable Long id) {
        UserDO user = this.userService.getUserById(id);
        return CommonResult.success(user, "查询成功");
    }

    @GetMapping("/test")
    public CommonResult<?> test3() {
        return CommonResult.success(age, "查询成功");
    }
}
