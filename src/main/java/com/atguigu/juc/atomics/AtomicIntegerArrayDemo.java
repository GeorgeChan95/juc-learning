package com.atguigu.juc.atomics;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @auther zzyy
 * @create 2021-03-18 16:42
 */
public class AtomicIntegerArrayDemo {
    public static void main(String[] args) {
        // 方式一：指定数组长度
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[5]);
        // 方式二：指定数组长度
        //AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);
        // 方式三：指定数组长度并初始化内容
        //AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[]{1,2,3,4,5});

        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.println(atomicIntegerArray.get(i));
        }
        System.out.println();
        System.out.println();
        System.out.println();
        int tmpInt = 0;

        // 指定下标设置内容
        tmpInt = atomicIntegerArray.getAndSet(0, 1122);
        System.out.println(tmpInt + "\t" + atomicIntegerArray.get(0)); // 0 1122
        // 指定下标元素自增
        atomicIntegerArray.getAndIncrement(1);
        atomicIntegerArray.getAndIncrement(1);
        tmpInt = atomicIntegerArray.getAndIncrement(1);
        // 打印下标元素
        System.out.println(tmpInt + "\t" + atomicIntegerArray.get(1)); // 2 3

    }
}
