package com.example.threadpool.impl;

import com.example.threadpool.MyThreadPool;
import com.example.threadpool.MyThreadRejectHandle;

/**
 * @Author: Niu
 * @Date: 2025/5/16 08:36
 * @Description: 丢弃任务拒绝策略
 */
public class DiscardRejectHandleImpl implements MyThreadRejectHandle {
    @Override
    public void rejectHandle(Runnable runnable, MyThreadPool myThreadPool) {
        // 丢弃任务
        myThreadPool.discardTask();
        // 重新执行
        myThreadPool.execute(runnable);
    }
}
