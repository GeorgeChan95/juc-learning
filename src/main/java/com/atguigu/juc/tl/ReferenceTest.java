package com.atguigu.juc.tl;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author George Chan
 * @date 2024/10/2 14:46
 * <p></p>
 */
public class ReferenceTest {
    public static void main(String[] args) {
        // 设置内存: -Xms10m -Xmx10m
        ReferenceQueue<MyObject2> referenceQueue = new ReferenceQueue();
        PhantomReference<MyObject2> phantomReference = new PhantomReference<>(new MyObject2(), referenceQueue);
        //System.out.println(phantomReference.get());

        List<byte[]> list = new ArrayList<>();

        new Thread(() -> {
            while (true) {
                list.add(new byte[1 * 1024 * 1024]);
                try {
                    TimeUnit.MILLISECONDS.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(phantomReference.get()); // null
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                Reference<? extends MyObject2> reference = referenceQueue.poll();
                if (reference != null) {
                    System.out.println("***********有虚对象加入队列了");
                }
            }
        }, "t2").start();

        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class MyObject2 {
    //一般这个方法工作中不用
    @Override
    protected void finalize() throws Throwable {
        System.out.println("------------- gc ,finalize() invoked");
    }
}
