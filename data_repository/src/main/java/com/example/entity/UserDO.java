package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Author:Niu
 * Date:2025/1/19 16:04
 * Description: nothing.
 */
@Data
@Entity(name = "user")
@Table(name = "users")
public class UserDO {

    @Id
    //自增
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String password;
    private int age;
}
