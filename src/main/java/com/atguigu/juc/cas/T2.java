package com.atguigu.juc.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName T2
 * @Description TODO
 * @Author George
 * @Date 2024/9/26 19:39
 */
public class T2 {
    AtomicInteger atomicInteger = new AtomicInteger();

    public int getAtomicInteger() {
        return atomicInteger.get();
    }

    public void setAtomicInteger() {
        atomicInteger.getAndIncrement();
    }
}
