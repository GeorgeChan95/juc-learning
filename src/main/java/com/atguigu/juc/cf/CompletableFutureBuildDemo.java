package com.atguigu.juc.cf;

import java.util.concurrent.*;

/**
 * @ClassName CompletableFutureBuildDemo
 * @Description CompletableFuture四个静态方法演示
 * @Author George
 * @Date 2024/9/19 9:57
 */
public class CompletableFutureBuildDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 自定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                20,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());


        // 无返回值，默认线程池
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "无返回值，默认线程池：ForkJoinPool");
        });
        System.out.println("future1 ==> " + future1.get());
        /*
        * 打印：
        ForkJoinPool.commonPool-worker-1无返回值，默认线程池：ForkJoinPool
        future1 ==> null
        * */
        System.out.println("----------------------------------------------------------");

        // 无返回值，自定义线程池
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "无返回值，自定义线程池");
        }, threadPoolExecutor);
        System.out.println("future2 ==> " + future2.get());
        /*
        * 打印：
        pool-1-thread-1无返回值，自定义线程池
        future2 ==> null
        * */
        System.out.println("----------------------------------------------------------");

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "有返回值，默认线程池：ForkJoinPool");
            return "有返回值，默认线程池：ForkJoinPool";
        });
        System.out.println("future3 ==> " + future3.get());
        /*
        * 打印：
        ForkJoinPool.commonPool-worker-1有返回值，默认线程池：ForkJoinPool
        future3 ==> 有返回值，默认线程池：ForkJoinPool
        * */
        System.out.println("----------------------------------------------------------");

        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "有返回值，自定义线程池");
            return "有返回值，自定义线程池";
        }, threadPoolExecutor);
        System.out.println("future4 ==> " + future4.get());
        /*
        * 打印：
        pool-1-thread-1有返回值，自定义线程池
        future4 ==> 有返回值，自定义线程池
        * */

        // 关闭线程池
        threadPoolExecutor.shutdown();
    }
}
