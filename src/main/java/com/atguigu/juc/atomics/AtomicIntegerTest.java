package com.atguigu.juc.atomics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName AtomicIntegerTest
 * @Description TODO
 * @Author George
 * @Date 2024/9/28 14:19
 */
public class AtomicIntegerTest {

    private static int size = 50; // 50个线程

    private static CountDownLatch countDownLatch = new CountDownLatch(50);
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        atomicInteger.getAndIncrement();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "t" + i).start();
        }

        // 方式一：等待线程运行结果后，再打印
//        TimeUnit.SECONDS.sleep(2);

        // 方式二：使用 countDownLatch
        countDownLatch.await();

        System.out.println("运行结果：" + atomicInteger.get());
    }
}
