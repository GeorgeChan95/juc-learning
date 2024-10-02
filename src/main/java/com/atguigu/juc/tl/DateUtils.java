package com.atguigu.juc.tl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

/**
 * @author George Chan
 * @date 2024/10/2 07:31
 * <p></p>
 */
public class DateUtils {

    private static final ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"));


    /**
     * 模拟并发环境下使用SimpleDateFormat的parse方法将字符串转换成Date对象
     *
     * @param stringDate
     * @return
     * @throws Exception
     */
    public static Date parseDate(String stringDate) throws Exception {
        SimpleDateFormat sdf = threadLocal.get();
//        System.out.println("执行线程:" + Thread.currentThread().getName());
        return sdf.parse(stringDate);
    }

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                try {
                    System.out.println(parseDate("2020-11-11 11:11:11"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }

}
