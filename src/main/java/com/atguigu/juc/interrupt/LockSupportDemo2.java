package com.atguigu.juc.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName LockSupportDemo2
 * @Description TODO
 * @Author George
 * @Date 2024/9/23 15:14
 */
public class LockSupportDemo2 {
    public static void main(String[] args) throws InterruptedException {
//        m1();
//        m2();

        error1();
    }

/**
 * 连续执行 unpark(), 相当于只执行了一次 unpark()
 */
private static void error1() {
    Thread t1 = new Thread(() -> {
        System.out.println(Thread.currentThread().getName() + "\t" + "---come in");
        System.out.println(Thread.currentThread().getName() + "\t" + "--- park1");
        LockSupport.park();
        System.out.println(Thread.currentThread().getName() + "\t" + "--- park2");
        LockSupport.park();
        System.out.println(Thread.currentThread().getName() + "\t" + "---被唤醒");
    }, "t1");
    t1.start();

    new Thread(() -> {
        LockSupport.unpark(t1);
        LockSupport.unpark(t1);
        System.out.println(Thread.currentThread().getName() + "\t" + "--- unpark1");
        System.out.println(Thread.currentThread().getName() + "\t" + "--- unpark2");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }, "t2").start();
}

    /**
     * 正常使用，无锁块要求
     * 先阻塞后释放，可以实现线程的阻塞和唤醒
     *
     * @throws InterruptedException
     */
    private static void m1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "---- come in");
            System.out.println(Thread.currentThread().getName() + "---- park阻塞");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "---- 继续执行");
        }, "t1");
        t1.start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "---- come in");
            LockSupport.unpark(t1);
        }, "t2").start();
    }


    /**
     * 先释放后阻塞，也可以实现线程的阻塞和唤醒
     *
     * @throws InterruptedException
     */
    private static void m2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "---- come in");
            System.out.println(Thread.currentThread().getName() + "---- park阻塞");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "---- 继续执行");
        }, "t1");
        t1.start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "---- come in");
            System.out.println(Thread.currentThread().getName() + "---- 给t1线程发通行证");
            LockSupport.unpark(t1);
        }, "t2").start();
    }
}
