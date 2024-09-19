package com.atguigu.juc.cf;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @ClassName FutureTaskDemo3
 * @Description TODO
 * @Author George
 * @Date 2024/9/18 17:31
 */
public class FutureTaskDemo3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyThread());
        Thread thread = new Thread(futureTask, "t1");
        // 启动一个新的线程
        thread.start();

        System.out.println(futureTask.get());
    }
}

class MyThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " come in ......");
        return "Hello Callable";
    }
}
