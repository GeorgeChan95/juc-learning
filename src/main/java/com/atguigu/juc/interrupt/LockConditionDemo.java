package com.atguigu.juc.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName LockConditionDemo
 * @Description TODO
 * @Author George
 * @Date 2024/9/23 14:29
 */
public class LockConditionDemo {

    public static void main(String[] args) throws InterruptedException {
//        normal();
        error1();
    }

/**
 * 正确使用 Lock 的 await / signal 实现对线程的等待和唤醒
 * @throws InterruptedException
 */
private static void normal() throws InterruptedException {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    new Thread(() -> {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "---- t1 释放锁");
            condition.await();
            System.out.println(Thread.currentThread().getName() + "---- t1 重新获取锁");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }, "t1").start();

    TimeUnit.SECONDS.sleep(1);

    new Thread(() -> {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "---- t2唤醒t1线程");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }, "t2").start();
}


    private static void error1() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "---- t1 释放锁");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "---- t1 重新获取锁");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "---- t2唤醒t1线程");
                condition.signal();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }


}
