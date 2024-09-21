package com.atguigu.juc.cf;


import java.util.concurrent.*;

/**
 * @auther zzyy
 * @create 2021-03-02 11:56
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
//        thenApplyDemo();

        handleDemo();
    }

private static void handleDemo() throws InterruptedException {
    // 自定义线程池
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            4,
            5,
            50,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    CompletableFuture.supplyAsync(() -> {
        int result = ThreadLocalRandom.current().nextInt(10);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (result > 1) {
            int i = 10 / 0;
        }
        return 10;
    }, threadPool).handle((v, e) -> {
        if (e == null) {
            System.out.println("handle1执行完成，结果：" + v);
        } else {
            System.out.println("handle1执行异常");
        }
        return v + 1;
    }).handle((v, e) -> {
        if (e == null) {
            System.out.println("handle2执行完成，结果：" + v);
        } else {
            System.out.println("handle2执行异常");
        }
        return "abc";
    }).handle((v, e) -> {
        if (e == null) {
            System.out.println("handle3执行完成，结果：" + v);
        } else {
            System.out.println("handle3执行异常");
        }
        return "xyz";
    }).whenCompleteAsync((v, e) -> {
        if (e == null) {
            System.out.println(Thread.currentThread().getName() + " whenComplete执行完成，结果：" + v);
        }
    }, threadPool).exceptionally(e -> {
        e.printStackTrace();
        String msg = String.format("执行出现异常，原因：%s, 异常信息：%s", e.getCause(), e.getMessage());
        System.out.println(msg);
        return "error";
    });

    TimeUnit.SECONDS.sleep(5);

    // 关闭线程池
    threadPool.shutdown();

}

    private static void thenApplyDemo() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (result > 5) {
                int i = 10 / 0;
            }
            return 1;
        }, threadPoolExecutor).thenApply(data -> data + 2).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("执行完成，结果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            String msg = String.format("执行出现异常，原因：%s, 异常信息：%s", e.getCause(), e.getMessage());
            System.out.println(msg);
            return -1;
        });

        System.out.println("执行结束，结果：" + completableFuture.join());

        // 关闭自定义线程池
        threadPoolExecutor.shutdown();

        // 主线程停留3秒
        TimeUnit.SECONDS.sleep(3);
    }
}
