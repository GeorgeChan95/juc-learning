package com.atguigu.juc.lockupgrade;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther zzyy
 * @create 2021-03-27 15:19
 * 锁粗化
 * 假如方法中首尾相接，前后相邻的都是同一个锁对象，那JIT编译器就会把这几个synchronized块合并成一个大块，
 * 加粗加大范围，一次申请锁使用即可，避免次次的申请和释放锁，提升了性能
 */
public class LockBigDemo {
    static Object objectLock = new Object();

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println("11111");
                System.out.println("22222");
                System.out.println("33333");
            }
        }, "a").start();

        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println("44444");
                System.out.println("55555");
                System.out.println("66666");
            }
        }, "b").start();
    }
}
