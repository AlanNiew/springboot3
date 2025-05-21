package com.example.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Niu
 * @Date: 2025/5/16 09:07
 * @Description: 自定义锁
 */
public class MyLock {

    // 锁状态
    private final static AtomicInteger state = new AtomicInteger(0);
    private Thread owner = null;
    // 队列头结点
    private final AtomicReference<Node> head = new AtomicReference<>(new Node());
    // 队列尾结点
    private final AtomicReference<Node> tail = new AtomicReference<>(head.get());

    // 加锁
    void lock(){
        if (state.get() == 0){
            // 直接上锁
            if (state.compareAndSet(0,1)){
                // 上锁成功
                System.out.println("线程"+Thread.currentThread().getName()+"直接拿到了锁");
                owner = Thread.currentThread();
                return;
            }
        }else {
            if (owner == Thread.currentThread()){
                // 可重入锁
                System.out.println("线程"+Thread.currentThread().getName()+"拿到可重入锁,次数："+state.incrementAndGet());
                return;
            }
        }
        // 加入队列尾端,并等待
        Node current = new Node();
        current.thread = Thread.currentThread();
        while (true){
            Node curTail = this.tail.get();
            if (this.tail.compareAndSet(curTail,current)){
                // 加入队列成功
                System.out.println("线程"+Thread.currentThread().getName()+"加入链表尾部");
                // 置于队列尾端
                current.pre = curTail;
                curTail.next = current;
                break;
            }
        }
        while (true){
            if (state.compareAndSet(0,1) && current.pre == this.head.get()){
                System.out.println("线程"+Thread.currentThread().getName()+"被唤醒之后拿到了锁");
                this.owner = Thread.currentThread();
                head.set(current);
                current.pre.next = null;
                current.pre = null;
                break;
            }
            LockSupport.park();
        }
    }

     // 解锁
    void unlock(){
        //  释放锁
        if (Thread.currentThread() != owner){
            throw new RuntimeException("当前线程不是锁的拥有者");
        }
        int i = state.get();
        if (i > 1){
            int i1 = state.decrementAndGet();
            System.out.println("线程"+Thread.currentThread().getName()+"解锁了可重入锁,次数："+i1);
            return;
        }
        if (i <= 0){
            throw new RuntimeException("重入锁解锁失败！");
        }
        Node headNode = head.get();
        Node next = headNode.next;
        owner = null;
        state.set(0); // 释放锁
        if (next != null){
            System.out.println("线程"+Thread.currentThread().getName() +"唤醒了线程"+next.thread.getName());
            LockSupport.unpark(next.thread);
        }
    }

}
class Node {
    Node pre;
    Node next;
    Thread thread;
}
