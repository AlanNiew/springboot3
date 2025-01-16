package com.example.springboot3.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author:Niu
 * Date:2025/1/16 15:10
 */
public class MyUtil {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getKey() {
        return simpleDateFormat.format(new Date())+System.currentTimeMillis()/1000;
    }

    public static void main(String[] args) {
        System.out.println(getKey());
    }

    public static void main(String[] args) {
        System.out.println(getKey());
    }
}
