package com.atguigu.juc.cf;

import java.util.concurrent.*;

/**
 * @ClassName FutureTaskDemo1
 * @Description TODO
 * @Author George
 * @Date 2024/9/18 16:21
 */
public class FutureTaskDemo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("-----come in FutureTask");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "" + ThreadLocalRandom.current().nextInt(100);
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        //3秒钟后才出来结果，还没有计算你提前来拿(只要一调用get方法，对于结果就是不见不散，会导致阻塞)
        //System.out.println(Thread.currentThread().getName()+"\t"+futureTask.get());

        //3秒钟后才出来结果，我只想等待1秒钟，过时不候
        System.out.println(Thread.currentThread().getName() + "\t" + futureTask.get(1L, TimeUnit.SECONDS));

        System.out.println(Thread.currentThread().getName() + "\t" + " run... here");

    }
}

