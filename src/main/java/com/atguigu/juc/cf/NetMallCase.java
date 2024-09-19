package com.atguigu.juc.cf;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName NetMallCase
 * @Description TODO
 * @Author George
 * @Date 2024/9/19 20:18
 */
public class NetMallCase {

    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("pdd"),
            new NetMall("taobao"),
            new NetMall("dangdangwang"),
            new NetMall("tmall")
    );

    /**
     * 不使用多线程，获取图书在不同平台的价格
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByStep(List<NetMall> list, String productName) {
        List<String> result = list.stream().map(netMall -> {
            String str = String.format(productName + " in %s price is %.2f", netMall.getMallName(), netMall.calcPrice(productName));
            return str;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 使用多线程异步的从不同平台获取图书价格
     *
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByAsync(List<NetMall> list, String productName) {
        List<String> results = list.stream().map(netMall ->
                        CompletableFuture.supplyAsync(() -> String.format(productName + " in %s price is %.2f", netMall.getMallName(), netMall.calcPrice(productName))))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return results;
    }

    public static void main(String[] args) {
        // 不使用多线程
        long startTime = System.currentTimeMillis();
        List<String> resuts = getPriceByStep(list, "mysql");
        for (String data : resuts) {
            System.out.println(data);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("不使用多线程，costTime: " + (endTime - startTime) + " 毫秒");

        // 使用异步多线程获取
        long startTime2 = System.currentTimeMillis();
        List<String> resuts2 = getPriceByAsync(list, "mysql");
        for (String data : resuts2) {
            System.out.println(data);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("使用异步多线程，costTime: " + (endTime2 - startTime2) + " 毫秒");
    }

}

class NetMallData {
    @Getter
    private String mallName;

    public NetMallData(String mallName) {
        this.mallName = mallName;
    }

    public double calcPrice(String productName) {
        //检索需要1秒钟
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}