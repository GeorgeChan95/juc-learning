package com.atguigu.juc.rwlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @ClassName TestStampedLock
 * @Description StampedLock简单使用
 * @Author George
 * @Date 2024/10/19 11:08
 */
public class TestStampedLock {
    // 声明邮戳锁
    private StampedLock lock = new StampedLock();
    private volatile int num = 10;

    public static void main(String[] args) throws InterruptedException {
        TestStampedLock test = new TestStampedLock();

        Runnable readRun = () -> test.read();
        Runnable writeRun = () -> test.write(5);

        // 读数据线程
        Thread thread1 = new Thread(readRun, "thread1");
        // 写数据线程
        Thread thread2 = new Thread(writeRun, "thread2");
        // 读数据线程
        Thread thread3 = new Thread(readRun, "thread3");

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread1.join();
            thread1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * 读数据
     */
    public void read() {
        long stamp = lock.tryOptimisticRead();
        try {
            System.out.println(Thread.currentThread().getName() + "开始读数据");
            // 初次尝试乐观读
            int currentNum = num;
            // 模拟读取时间延迟
            TimeUnit.MILLISECONDS.sleep(300);
            if (!lock.validate(stamp)) {
                // 乐观读失败，开始悲观读
                stamp = lock.readLock();
                try {
                    // 再次读取内容
                    currentNum = num;
                } finally {
                    // 释放悲观读
                    lock.unlockRead(stamp);
                }
            }
            System.out.println(Thread.currentThread().getName() + "读取到的数值：" + currentNum);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " 发生异常，异常消息：" + e.getMessage());
            // 终端线程执行
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 写数据
     * @param number
     */
    public void write(int number) {
        long stamp = lock.writeLock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始写数据");
            num += number;
            System.out.println(Thread.currentThread().getName() + "将数据写为：" + num);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
