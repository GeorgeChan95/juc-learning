package com.atguigu.juc.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther zzyy
 * @create 2021-03-27 15:46
 */
public class AQSDemo {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();//非公平锁

        // A线程先抢到锁，执行业务
        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("----come in A");
                //暂停1分钟线程
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                reentrantLock.unlock();
            }
        }, "A").start();

        // B线程等待，进入AQS队列(FIFO)，等待着A运行结束，尝试去抢占锁。
        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("----come in B");
            } finally {
                reentrantLock.unlock();
            }
        }, "B").start();


        // C线程等待，进入AQS队列(FIFO)，等待着A运行结束，此时前面是B线程
        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("----come in C");
            } finally {
                reentrantLock.unlock();
            }
        }, "C").start();

        // D线程等待，进入AQS队列(FIFO)，等待着A运行结束，此时前面是C线程
        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("----come in C");
            } finally {
                reentrantLock.unlock();
            }
        }, "D").start();

        // E线程等待，进入AQS队列(FIFO)，等待着A运行结束，此时前面是D线程
        new Thread(() -> {
            reentrantLock.lock();
            try {
                System.out.println("----come in C");
            } finally {
                reentrantLock.unlock();
            }
        }, "E").start();

    }
}
