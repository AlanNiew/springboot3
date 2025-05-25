package com.example.proxy.mybaits.mapper;

import com.example.proxy.mybaits.annontion.Param;
import com.example.proxy.mybaits.entity.User;

/**
 * @Author: Niu
 * @Date: 2025/5/22 14:18
 * @Description:
 */
public interface UserMapper {

    User queryUserById(@Param("id") int id);
    User queryUserByName(@Param("name") String name);

    User queryUserByNameAndId(@Param("id") int id,@Param("name") String name);
    User queryUserByNameAndPwd(@Param("name") String name,@Param("password") String pwd);
}
