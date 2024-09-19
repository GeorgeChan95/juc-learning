package com.atguigu.juc.cf;

import java.util.concurrent.*;

/**
 * @ClassName CompletableFutureUseDemo
 * @Description 演示CompletableFuture回调方法
 * @Author George
 * @Date 2024/9/19 10:18
 */
public class CompletableFutureUseDemo {
    public static void main(String[] args) throws InterruptedException {
        // 自定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,
                20,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " 线程运行中...");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (result > 5) {
                int i = 10 / 0;
            }
            return result;
        }, threadPoolExecutor).whenComplete((v, e) -> { // 当计算完成时调用
            if (e == null) {
                System.out.println(Thread.currentThread().getName() + " 线程计算完成，结果为：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            String msg = String.format("%s 线程计算出现异常，原因：%s, 错误信息：%s", Thread.currentThread().getName(), e.getCause(), e.getMessage());
            System.out.println(msg);
            return -1;
        });

        System.out.println(Thread.currentThread().getName() + " 线程先去完成其他任务");

        // 线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭：暂停3秒钟线程
        TimeUnit.SECONDS.sleep(2);
        // 关闭自定义线程池
        threadPoolExecutor.shutdown();
    }
}
/*
无异常时打印如下：
pool-1-thread-1 线程运行中...
main 线程先去完成其他任务
pool-1-thread-1 线程计算完成，结果为：2
* */

/*
有异常时打印如下：
pool-1-thread-1 线程运行中...
main 线程先去完成其他任务
java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
	at java.base/java.util.concurrent.CompletableFuture.encodeThrowable(CompletableFuture.java:315)
	at java.base/java.util.concurrent.CompletableFuture.completeThrowable(CompletableFuture.java:320)
	at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1770)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:842)
Caused by: java.lang.ArithmeticException: / by zero
	at com.atguigu.juc.cf.CompletableFutureUseDemo.lambda$main$0(CompletableFutureUseDemo.java:32)
	at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1768)
	... 3 more
pool-1-thread-1 线程计算出现异常，原因：java.lang.ArithmeticException: / by zero, 错误信息：java.lang.ArithmeticException: / by zero
* */