package com.atguigu.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Chain
 * @Description TODO
 * @Author George
 * @Date 2024/9/19 11:26
 */
public class Chain {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException { //抛出异常
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello 12345";
        });
//        System.out.println(completableFuture.get(3, TimeUnit.SECONDS));
        System.out.println(completableFuture.join());




    }
}
