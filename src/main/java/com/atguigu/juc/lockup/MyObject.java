package com.atguigu.juc.lockup;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author George Chan
 * @date 2024/10/6 12:27
 * <p></p>
 */
public class MyObject {
    public static void main(String[] args) {
        Object o = new Object();

        System.out.println("10进制hash码：" + o.hashCode());
        System.out.println("16进制hash码：" + Integer.toHexString(o.hashCode()));
        System.out.println("2进制hash码：" + Integer.toBinaryString(o.hashCode()));

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
