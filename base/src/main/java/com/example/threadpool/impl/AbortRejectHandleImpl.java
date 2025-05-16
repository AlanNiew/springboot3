package com.example.threadpool.impl;

import com.example.threadpool.MyThreadPool;
import com.example.threadpool.MyThreadRejectHandle;

/**
 * @Author: Niu
 * @Date: 2025/5/16 08:38
 * @Description: 中止拒绝策略
 */
public class AbortRejectHandleImpl implements MyThreadRejectHandle {
    @Override
    public void rejectHandle(Runnable runnable, MyThreadPool myThreadPool) {
        throw new RuntimeException("线程池已满，无法执行任务");
    }
}
