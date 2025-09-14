package com.example.threadpool;

import cn.hutool.core.date.DateTime;
import com.example.threadpool.impl.AbortRejectHandleImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Niu
 * @Date: 2025/5/15 15:41
 * @Description: 自定义线程池
 */
public class MyThreadPool {

    // 核心线程数
    private final int corePoolSize;
    // 最大线程数
    private final int maxPoolSize;

    private long keepAliveTime = 10; // 空闲时间，默认10秒
    private TimeUnit timeUnit = TimeUnit.SECONDS; // 时间单位
    private boolean isMonitor = true; // 是否开启监控

    private final List<Thread> coreThreads = new ArrayList<>(); // 核心线程
    private final List<Thread> workThreads = new ArrayList<>(); // 工作线程,辅助线程
    private final ArrayBlockingQueue<Runnable> runnableList; // 任务队列

    private MyThreadRejectHandle rejectHandle = new AbortRejectHandleImpl(); // 拒绝策略, 默认中止

    public MyThreadPool(int corePoolSize, int maxPoolSize, int queueSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.runnableList = new ArrayBlockingQueue<>(queueSize); // 任务队列
    }

    public MyThreadPool(int corePoolSize, int maxPoolSize, int queueSize, MyThreadRejectHandle rejectHandle) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.runnableList = new ArrayBlockingQueue<>(queueSize); // 任务队列
        this.rejectHandle = rejectHandle;
    }

    public MyThreadPool(int corePoolSize, int maxPoolSize, int queueSize, long keepAliveTime, TimeUnit timeUnit, MyThreadRejectHandle rejectHandle) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.runnableList = new ArrayBlockingQueue<>(queueSize); // 任务队列
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
    }
    public MyThreadPool(int corePoolSize, int maxPoolSize, int queueSize, long keepAliveTime, TimeUnit timeUnit, boolean isMonitor, MyThreadRejectHandle rejectHandle) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.runnableList = new ArrayBlockingQueue<>(queueSize); // 任务队列
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.isMonitor = isMonitor;
    }
    {
        monitor();
    }
    public void execute(Runnable runnable){
        if (coreThreads.size() < corePoolSize){ // 判断任务队列是否小于核心线程数
            // 创建核心线程
            CoreThread coreThread = new CoreThread(runnable);
            coreThreads.add(coreThread);
            coreThread.start();
            return;
        }
        if (runnableList.offer(runnable)){
            // 放入任务队列, 放入成功，返回true
            return;
        }
        // 线程数是否小于最大线程。
        if (coreThreads.size() + workThreads.size() < maxPoolSize){
            WorkerThread workerThread = new WorkerThread(runnable);
            workThreads.add(workerThread);
            workerThread.start();
        }else{
            rejectHandle.rejectHandle(runnable,this);
        }
    }

    // 监控
    public void monitor(){
        if (isMonitor){
            new Thread(()->{
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    System.out.println(DateTime.now() +"---- 核心线程数："+coreThreads.size()+" 工作线程数："+workThreads.size()+" 任务队列数："+runnableList.size());
                }
            },"monitor").start();
        }
    }

    class CoreThread extends Thread{

        private final Runnable firstRunnable;
        public CoreThread(Runnable firstRunnable){
            this.firstRunnable = firstRunnable;
        }

        @Override
        public void run() {
            // 首先执创建的线程任务
            firstRunnable.run();
            // 后续循环获取任务执行
            while (!Thread.currentThread().isInterrupted()){
                try {
                    // 获取任务, 没有任务进行阻塞
                    Runnable take = runnableList.take();
                    take.run();
                } catch (InterruptedException e) {
                    // 线程被中断时退出循环
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("线程被中断,"+e);
                }
            }
        }
    }


    class WorkerThread extends Thread{
        private final Runnable secondRunnable;
        public WorkerThread(Runnable secondRunnable){
            this.secondRunnable = secondRunnable;
        }
        @Override
        public void run() {
            // 首先执行创建的线程任务
            secondRunnable.run();
            while (true){
                try {
                    // 获取任务，等待 1s
                    Runnable runnable = runnableList.poll(keepAliveTime, timeUnit);
                    if (runnable != null){
                        runnable.run();
                    }else {
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("工作线程"+Thread.currentThread().getName()+"退出");
            // 线程退出时从工作线程列表中移除
            workThreads.remove(Thread.currentThread());
        }
    }

    //丢弃 任务队列中任务
    public void discardTask(){
        Runnable take;
        if ((take = runnableList.poll()) != null){
            System.out.println("丢弃任务："+take);
        }else{
            System.out.println("任务队列为空");
        }
    }
}
