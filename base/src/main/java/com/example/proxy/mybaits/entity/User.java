package com.example.proxy.mybaits.entity;

import com.example.proxy.mybaits.annontion.Table;
import lombok.Data;

/**
 * @Author: Niu
 * @Date: 2025/5/22 14:07
 * @Description:
 */
@Table(tableName = "user")
@Data
public class User {
    private Long id;
    private String name;
    private String password;
    private String email;
}
