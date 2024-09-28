package com.atguigu.juc.cas;

/**
 * @ClassName T1
 * @Description TODO
 * @Author George
 * @Date 2024/9/26 19:38
 */
public class T1 {
    volatile int number = 0;

    //读取
    public int getNumber() {
        return number;
    }

    //写入加锁保证原子性
    public synchronized void setNumber() {
        number++;
    }
}
