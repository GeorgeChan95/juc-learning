package com.atguigu.juc.tl;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author George Chan
 * @date 2024/10/2 13:56
 * <p></p>
 */
public class ThreadLocalPoolTest {
    public static void main(String[] args) throws InterruptedException {
        MyCar car = new MyCar();
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 6; i++) {
            executorService.submit(() -> {
                try {
                    int num = new Random().nextInt(10);
                    for (int j = 0; j < num; j++) {
                        car.saleCar();
                    }
                    System.out.println(Thread.currentThread().getName() + "卖出了:" + car.threadLocal.get() + "辆车");
                } finally {
                    car.threadLocal.remove();
                }
            });
        }

        executorService.shutdown();
    }
}

class MyCar {
    // 初始化ThreadLocal,设置初始值为:0
    ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    public void saleCar() {
        Integer value = threadLocal.get();
        value++;
        threadLocal.set(value);
        System.out.println(Thread.currentThread().getName() + "卖了:" + threadLocal.get() + "辆车");
    }
}
