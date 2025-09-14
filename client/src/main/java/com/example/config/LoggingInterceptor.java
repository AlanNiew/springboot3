package com.example.config;

import okhttp3.logging.HttpLoggingInterceptor;

public class LoggingInterceptor {
    public static HttpLoggingInterceptor create() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY); // 记录请求和响应体
    }
}