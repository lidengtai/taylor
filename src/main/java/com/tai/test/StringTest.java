package com.tai.test;

import java.io.UnsupportedEncodingException;

/**
 * @description: string常用方法
 * @author: Taylor
 * @date :  2021-03-23 09:04
 **/
public class StringTest {
    public static void main(String[] args) {
        String str = "my name is taylor !";

        try {
            byte[] bytes = str.getBytes("UTF-8");
            byte[] newByte = new byte[6];
            System.arraycopy(bytes,11,newByte,0,6);
            System.out.println(new String(bytes));
            System.out.println(new String(newByte));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
