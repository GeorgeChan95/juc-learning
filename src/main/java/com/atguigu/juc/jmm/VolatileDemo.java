package com.atguigu.juc.jmm;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName VolatileDemo
 * @Description TODO
 * @Author George
 * @Date 2024/9/26 10:10
 */

class MyNumber1 {
    public volatile int num = 0;

    public synchronized void addNum() {
        num++;
    }
}

public class VolatileDemo {
    public static void main(String[] args) throws InterruptedException {
        MyNumber1 myNumber1 = new MyNumber1();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myNumber1.addNum();
                }
            }, "t" + i).start();
        }

        TimeUnit.SECONDS.sleep(3);

        System.out.println("执行结束，num值为：" + myNumber1.num);
    }
}
