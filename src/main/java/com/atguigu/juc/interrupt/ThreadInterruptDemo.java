package com.atguigu.juc.interrupt;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName ThreadInterruptDemo
 * @Description 多种方式实现线程的中断
 * @Author George
 * @Date 2024/9/23 9:46
 */
public class ThreadInterruptDemo {
    static volatile boolean isStop = false;
    static AtomicBoolean flag = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {

        threadApiInterruptThread();

    }

    /**
     * 通过Thread类自带的中断api方法实现
     *
     * @throws InterruptedException
     */
    private static void threadApiInterruptThread() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("------- t1 run");
            }
            System.out.println("===== t1 is stop =====");
        }, "t1");

        t1.start();

        // 让线程t1运行50毫秒后，中断线程运行
        Thread.sleep(50);

        new Thread(() -> {
            // t1线程中断标识设置为true，等待县城自我中断
            t1.interrupt();
            System.out.println("------- t2 stop: " + true);
        }, "t2").start();
    }

    /**
     * 通过AtomicBoolean变量实现线程中断
     *
     * @throws InterruptedException
     */
    private static void atomicBooleanStopThread() throws InterruptedException {
        new Thread(() -> {
            while (!flag.get()) {
                System.out.println("------- t1 run");
            }
            System.out.println("===== t1 is stop =====");
        }, "t1").start();

        // 让线程t1运行50毫秒后，中断线程运行
        Thread.sleep(50);

        new Thread(() -> {
            flag.set(true);
            System.out.println("------- t2 stop: " + flag.get());
        }, "t2").start();
    }

    /**
     * volatile变量实现线程中断
     *
     * @throws InterruptedException
     */
    private static void volatileStopThread() throws InterruptedException {
        new Thread(() -> {
            while (!isStop) {
                System.out.println("------- t1 run");
            }
            System.out.println("===== t1 is stop =====");
        }, "t1").start();

        // 让线程t1运行50毫秒后，中断线程运行
        Thread.sleep(10);

        new Thread(() -> {
            isStop = true;
            System.out.println("------- t2 stop: " + isStop);
        }, "t2").start();
    }
}
