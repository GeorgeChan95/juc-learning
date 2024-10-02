package com.atguigu.juc.tl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author George Chan
 * @date 2024/10/2 06:52
 * <p></p>
 */
public class ThreadLocalTest {

    public static void main(String[] args) throws InterruptedException {
        MyHouse house = new MyHouse();
        new Thread(() -> {
            try {
                int num = new Random().nextInt(10);
                for (int i = 0; i < num; i++) {
                    house.saleHouse();
                }
                System.out.println(Thread.currentThread().getName() + "卖出了:" + house.threadLocal.get() + "套房子");
            } finally {
                house.threadLocal.remove();
            }
        }, "销售1").start();

        new Thread(() -> {
            try {
                int num = new Random().nextInt(10);
                for (int i = 0; i < num; i++) {
                    house.saleHouse();
                }
                System.out.println(Thread.currentThread().getName() + "卖出了:" + house.threadLocal.get() + "套房子");
            } finally {
                house.threadLocal.remove();
            }
        }, "销售2").start();

        new Thread(() -> {
            try {
                int num = new Random().nextInt(10);
                for (int i = 0; i < num; i++) {
                    house.saleHouse();
                }
                System.out.println(Thread.currentThread().getName() + "卖出了:" + house.threadLocal.get() + "套房子");
            } finally {
                house.threadLocal.remove();
            }
        }, "销售3").start();

        TimeUnit.SECONDS.sleep(1);
    }

}


class MyHouse {
    // 初始化ThreadLocal,设置初始值为:0
    ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    public void saleHouse() {
        Integer value = threadLocal.get();
        value++;
        threadLocal.set(value);
    }
}
