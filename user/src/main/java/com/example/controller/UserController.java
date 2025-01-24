package com.example.controller;

import com.example.api.CommonPage;
import com.example.api.CommonResult;
import com.example.entity.UserDO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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
    public CommonResult<?> test3() throws InterruptedException {
        //延迟
        int time = ThreadLocalRandom.current().nextInt(2000, 3000);
        TimeUnit.MILLISECONDS.sleep(time);
        return CommonResult.success(age, "查询成功");
    }

    @GetMapping("/page")
    public CommonResult<?> pageList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<UserDO> userDOS = userService.pageList(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(userDOS));
    }
}
