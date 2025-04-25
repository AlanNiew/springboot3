package com.example.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Niu
 * @Date: 2025/4/24 16:19
 * @Description: 本地缓存工具类
 */
public class LocalCacheUtil {

    public static final String CACHE_KEY_PREFIX = "cache_";
    public static void main(String[] args) {
        // 创建本地缓存，设置最大容量100条，写入后5分钟过期
        Cache<String, String> cache = Caffeine.newBuilder()
                // 初始化缓存大小
                .initialCapacity(5)
                // 最大缓存数量
                .maximumSize(10)
                // 最后写入后30秒过期
                .expireAfterWrite(30, TimeUnit.SECONDS)
                // 最后访问后10秒过期
                .expireAfterAccess(10, TimeUnit.SECONDS)
                // 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                // 监听缓存移除
                .removalListener((key, value, cause) -> {
                    System.out.println("key:" + key + " value:" + value + " cause:" + cause);
                })
                .recordStats()
                .build();

        // 添加缓存
        cache.put("user:1001", "小明");

        // 获取缓存
        String value = cache.getIfPresent("user:1001");
        System.out.println("从缓存中获取：" + value);
        // 使用 CacheLoader 方式懒加载
        String lazyValue = cache.get("user:1002", key -> {
            // 模拟从数据库查询
            return "小红";
        });
        System.out.println("懒加载获取：" + lazyValue);
    }
}
