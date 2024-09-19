package com.atguigu.juc.cf;

import java.util.concurrent.*;

/**
 * @ClassName FutureTaskDemo4
 * @Description Futrue + 线程池的组合
 * @Author George
 * @Date 2024/9/19 8:59
 */
public class FutureTaskDemo4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "-----come in FutureTask");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "" + ThreadLocalRandom.current().nextInt(100);
        });

        // 异步提交任务
        executorService.submit(futureTask);

        System.out.println(Thread.currentThread().getName() + "\t" + "线程完成任务");

        /**
         * 用于阻塞式获取结果,如果想要异步获取结果,通常都会以轮询的方式去获取结果
         */
        while (true) {
            if (futureTask.isDone()) {
                System.out.println(futureTask.get());
                break;
            }
        }

        // 关闭线程池
        executorService.shutdown();
    }
}
