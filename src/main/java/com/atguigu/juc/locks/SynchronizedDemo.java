package com.atguigu.juc.locks;

/**
 * @ClassName SynchronizedDemo
 * @Description TODO
 * @Author George
 * @Date 2024/9/21 15:04
 */
public class SynchronizedDemo {
    Object object = new Object();

    public void m1(){
        synchronized (object){
            System.out.println("-----hello synchronized code block");
        }
    }

    public static void main(String[] args) {

    }
}
