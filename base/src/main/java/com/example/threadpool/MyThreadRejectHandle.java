package com.example.threadpool;

/**
 * @Author: Niu
 * @Date: 2025/5/16 08:35
 * @Description: 自定义线程池拒绝策略
 */
public interface MyThreadRejectHandle {

    void rejectHandle(Runnable runnable, MyThreadPool myThreadPool);
}
