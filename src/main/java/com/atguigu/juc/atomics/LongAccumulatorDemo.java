package com.atguigu.juc.atomics;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

/**
 * @ClassName LongAccumulatorDemo
 * @Description TODO
 * @Author George
 * @Date 2024/9/28 17:15
 */
public class LongAccumulatorDemo {
    LongAdder longAdder = new LongAdder();

    public void add_LongAdder() {
        longAdder.increment();
    }

    //LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y,0);
    // 初始值为10，每次调用，将传进来的参数与10相减
    LongAccumulator longAccumulator = new LongAccumulator(new LongBinaryOperator() {
        @Override
        public long applyAsLong(long left, long right) {
            return left - right;
        }
    }, 10); // 10 是初始值

    public void add_LongAccumulator() {
        longAccumulator.accumulate(1);
    }

    public static void main(String[] args) {
        LongAccumulatorDemo demo = new LongAccumulatorDemo();
        // 第一次操作
        demo.add_LongAccumulator();
        // 第二次操作
        demo.add_LongAccumulator();
        // 打印结果
        System.out.println("执行结果：" + demo.longAccumulator.longValue());
    }

}
