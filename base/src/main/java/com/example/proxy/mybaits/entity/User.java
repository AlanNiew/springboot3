package com.example.proxy.mybaits.entity;

import com.example.proxy.mybaits.annontion.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @Author: Niu
 * @Date: 2025/5/22 14:07
 * @Description:
 */
@Table(tableName = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private String email;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
