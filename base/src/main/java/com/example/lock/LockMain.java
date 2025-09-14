package com.example.lock;

import cn.hutool.core.thread.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: Niu
 * @Date: 2025/5/21 09:51
 * @Description:
 */
public class LockMain {

    private static int tmp = 10;

    public static void main(String[] args) {
        MyLock lock = new MyLock();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    lock.lock();
                    ThreadUtil.sleep(ThreadLocalRandom.current().nextInt(100,200)+100);
                    if (tmp > 0) {
                        --tmp;
                    }
                }
                for (int j = 0; j < 3; j++) {
                    lock.unlock();
                }
            }, "Lock-Test" + i);
            threads.add(thread);
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("剩余：" + tmp);
    }

}
