package com.atguigu.juc.bashthread;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 演示守护线程和用户线程
 * </p>
 */
public class DaemonDemo {
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " come in：\t"
                    + (Thread.currentThread().isDaemon() ? "守护线程" : "用户线程"));
            while (true) {

            }
        }, "a");
        a.setDaemon(true); // daemon设置为true，表示该线程为守护线程
        // setDaemon 必须在 start() 方法之前
        a.start();


        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "\t" + " ----task is over");
    }
}
