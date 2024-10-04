package com.atguigu.juc.head;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author George Chan
 * @date 2024/10/4 11:38
 * <p></p>
 */
public class MyObject {
    public static void main(String[] args){

        Object o = new Object();
        System.out.println( ClassLayout.parseInstance(o).toPrintable());


        //VM的细节详细情况
//        System.out.println(VM.current().details());
//        //所有的对象分配的字节都是8的整数倍。
//        System.out.println(VM.current().objectAlignment());
    }
}
