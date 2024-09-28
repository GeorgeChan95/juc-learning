package com.atguigu.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName MySpinLock
 * @Description TODO
 * @Author George
 * @Date 2024/9/27 19:32
 */
public class MySpinLock {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock() {
        // 当没有替换成当前线程，则表示获取锁失败，线程自旋
        while (!atomicReference.compareAndSet(null, Thread.currentThread())) {

        }
        System.out.println(Thread.currentThread().getName() + "获取锁成功");
    }

    public void unlock() {
        while (atomicReference.compareAndSet(Thread.currentThread(), null)) {
            System.out.println(Thread.currentThread().getName() + "解锁成功");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MySpinLock lock = new MySpinLock();
        new Thread(() -> {
            // 获取锁
            lock.lock();
            try {
                // 阻塞3秒
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 解锁
            lock.unlock();
        }, "t1").start();

        // 程序暂停1秒， 保证t1先拿到锁
        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            lock.lock();
            lock.unlock();
        }, "t2").start();
    }
}
