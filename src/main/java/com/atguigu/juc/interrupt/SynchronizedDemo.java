package com.atguigu.juc.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName SynchronizedDemo
 * @Description TODO
 * @Author George
 * @Date 2024/9/23 13:56
 */
public class SynchronizedDemo {

    static Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
//        normal();
//        error1();
        error2();
    }

    /**
     * 正确使用 wait / notify 实现线程的等待和唤醒
     *
     * @throws InterruptedException
     */
    private static void normal() throws InterruptedException {
        new Thread(() -> {
            synchronized (object) {
                try {
                    System.out.println(Thread.currentThread().getName() + " ---- t1释放锁");
                    // wait方法会释放锁，给其他线程获取锁的机会
                    object.wait();
                    System.out.println(Thread.currentThread().getName() + " ---- t1重新获取锁");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "---- notify");
                object.notify(); // notify执行，让 t1 线程重新获取锁
            }
        }, "t2").start();
    }

    /**
     * 错误方式—： 去掉 synchronized
     *
     * @throws InterruptedException
     */
    private static void error1() throws InterruptedException {
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ---- t1释放锁");
                // wait方法会释放锁，给其他线程获取锁的机会
                object.wait();
                System.out.println(Thread.currentThread().getName() + " ---- t1重新获取锁");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "---- notify");
            object.notify(); // notify执行，让 t1 线程重新获取锁
        }, "t2").start();
    }

/**
 * 错误使用二：把notify和wait的执行顺序对换
 *
 * @throws InterruptedException
 */
private static void error2() throws InterruptedException {
    new Thread(() -> {
        try {
            TimeUnit.SECONDS.sleep(2); // t1 线程先停2秒，等待t2线程执行结束
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (object) {
            try {
                System.out.println(Thread.currentThread().getName() + " ---- t1释放锁");
                // wait方法会释放锁，给其他线程获取锁的机会
                object.wait();
                System.out.println(Thread.currentThread().getName() + " ---- t1重新获取锁");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }, "t1").start();

    TimeUnit.SECONDS.sleep(1);

    new Thread(() -> {
        synchronized (object) {
            System.out.println(Thread.currentThread().getName() + "---- notify");
            object.notify(); // notify执行，让 t1 线程重新获取锁
        }
    }, "t2").start();
}
}
