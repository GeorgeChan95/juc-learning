package com.atguigu.juc.lockup;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author George Chan
 * @date 2024/10/6 14:58
 * <p>
 *     验证Jdk1.8 默认开启偏向锁
 *     设置 VM option : -XX:BiasedLockingStartupDelay=0
 *
 *     验证轻量级锁:
 *     设置VM option : -XX:-UseBiasedLocking
 * </p>
 */
public class BiasedLockDemo {

    private static Object objectLock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        }).start();
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        }).start();
        new Thread(() -> {
            synchronized (objectLock) {
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        }).start();
    }
}
