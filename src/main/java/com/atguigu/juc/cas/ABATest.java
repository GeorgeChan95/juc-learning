package com.atguigu.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @ClassName ABATest
 * @Description TODO
 * @Author George
 * @Date 2024/9/27 19:49
 */
public class ABATest {

    // 初始值：100   初始版本号：1
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + " 默认获取到的值：" + stampedReference.getReference() + "\t默认版本号：" + stamp);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 修改值，将版本号加1
            boolean flag1 = stampedReference.compareAndSet(100, 101, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "第一次修改：" + flag1);
            // 修改值，将版本号加1
            boolean flag2 = stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "第二次修改：" + flag2);

            System.out.println("修改后的值：" + stampedReference.getReference() + "\t版本号：" + stampedReference.getStamp());
        }, "t1").start();


        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + " 默认获取到的值：" + stampedReference.getReference() + "\t默认版本号：" + stamp);

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 修改值，将版本号加1
            boolean flag = stampedReference.compareAndSet(100, 2024, stamp, stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "修改：" + flag);

            System.out.println("修改后的值：" + stampedReference.getReference() + "\t版本号：" + stampedReference.getStamp());
        }, "t2").start();
    }
}
