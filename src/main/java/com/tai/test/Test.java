package com.tai.test;

/**
 * @description: 飘号：变量互换
 * @author: Taylor
 * @date :  2020-11-12 09:58
 **/
public class Test {
    public static void main(String[] args) {
        int a = 30;
        int b = 80;

        a = a ^ b;
        System.out.println(a);

        b = a ^ b;
        System.out.println(b);

        a = a^ b;
        System.out.println(a);


        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }).start();
    }
}
