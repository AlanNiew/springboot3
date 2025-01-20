package com.example.client;

import com.example.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Author:Niu
 * Date:2025/1/20 09:33
 * Description: nothing.
 */
@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/user/byId/{id}")
    CommonResult<?> getUserById(@PathVariable("id") Long id);

    @GetMapping("/user/all")
    CommonResult<?> getAllUser();
}
