package com.atguigu.juc.cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName AtomicReferenceTest
 * @Description TODO
 * @Author George
 * @Date 2024/9/27 19:13
 */
public class AtomicReferenceTest {

    public static void main(String[] args) {
        User1 user1 = new User1("张三", 20);
        User1 user2 = new User1("george", 22);

        AtomicReference<User1> atomicUser1 = new AtomicReference<>();
        atomicUser1.set(user1);
        System.out.println("当前人员：" + atomicUser1.get());
        // 第一次比较替换，张三 换成 george， 结果成功
        System.out.println(atomicUser1.compareAndSet(user1, user2) + "\t" + atomicUser1.get());
        // 第二次比较替换，张三 换成 george， 结果失败
        System.out.println(atomicUser1.compareAndSet(user1, user2) + "\t" + atomicUser1.get());
    }
}


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class User1 {
    private String name;
    private Integer age;
}
