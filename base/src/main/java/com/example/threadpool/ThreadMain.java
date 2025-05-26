package com.example.threadpool;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: ${USER}
 * @Date: ${DATE} ${TIME}
 * @Description:
 */
public class ThreadMain {

    private volatile int count = 0;
    public static void main(String[] args) {
        int [] arr = {100};
        // 创建线程池
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                ThreadUtil.sleep(ThreadLocalRandom.current().nextInt(100,2000));
                arr[0] = arr[0]-1;
                System.out.println("线程"+Thread.currentThread().getName() +"处理后:"+arr[0]);
            }).start();
        }
        new Thread(()->{
            while (true){
                ThreadUtil.sleep(1000);
                System.out.println(arr[0]);
            }
        }).start();
    }
}