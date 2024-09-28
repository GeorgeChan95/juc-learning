package com.atguigu.juc.atomics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @ClassName AtomicIntegerFieldUpdaterTest
 * @Description TODO
 * @Author George
 * @Date 2024/9/28 16:46
 */
public class AtomicIntegerFieldUpdaterTest {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        // 创建10个线程，每个线程操作1000次
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    bank.add(bank);
                }
            }).start();
        }

        // 主线程等待其它线程运行结束
        TimeUnit.SECONDS.sleep(3);

        System.out.println("账户还剩：" + bank.money);
    }
}

class Bank {
    //以一种线程安全的方式操作非线程安全对象内的某些字段
    //1 更新的对象属性必须使用 public volatile 修饰符。
    public volatile int money = 0;

    public String name;

    //2 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须
    // 使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
    AtomicIntegerFieldUpdater<Bank> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Bank.class, "money");

    public void add(Bank bank) {
        // 每次增加10
        fieldUpdater.getAndAdd(bank, 10);
    }
}
