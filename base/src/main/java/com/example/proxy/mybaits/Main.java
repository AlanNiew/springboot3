package com.example.proxy.mybaits;

import com.example.proxy.mybaits.entity.User;
import com.example.proxy.mybaits.factory.MySqlQueryFactory;
import com.example.proxy.mybaits.mapper.UserMapper;

/**
 * @Author: Niu
 * @Date: 2025/5/22 13:46
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        MySqlQueryFactory mySqlQueryFactory = new MySqlQueryFactory();
        UserMapper userMapper = mySqlQueryFactory.getMapper(UserMapper.class);
//        User user = userMapper.queryUserByName("tom");
//        User user = userMapper.queryUserByNameAndId(3,"tom");
        User user = userMapper.queryUserByNameAndPwd("tom", "123456");
        System.out.println(user);
    }
}
