package com.example.springboot3.util;

import java.time.LocalDateTime;

/**
 * Author:Niu
 * Date:2025/1/16 15:10
 */
public class MyUtil {

    public static String getKey() {
        return LocalDateTime.now().toString()+System.currentTimeMillis()/1000;
    }

    public static void main(String[] args) {
        System.out.println(getKey());
    }
}
