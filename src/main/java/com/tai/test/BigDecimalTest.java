package com.tai.test;

import java.math.BigDecimal;

/**
 * @description: 数字型方法练习
 * @author: Taylor
 * @date :  2021-03-23 11:22
 **/
public class BigDecimalTest {

    public static void main(String[] args) {


        /**
         * 1）System.out.println()中的数字默认是double类型的，double类型小数计算不精准。
         *
         * 2）使用BigDecimal类构造方法传入double类型时，计算的结果也是不精确的！
         *
         * 因为不是所有的浮点数都能够被精确的表示成一个double 类型值，有些浮点数值不能够被精确的表示成 double 类型值，因此它会被表示成与它最接近的 double 类型的值。
         * 必须改用传入String的构造方法。这一点在BigDecimal类的构造方法注释中有说明。
         */
        System.out.println( new BigDecimal("13.334").setScale(2, BigDecimal.ROUND_HALF_DOWN));
        System.out.println( new BigDecimal("13.335").setScale(2, BigDecimal.ROUND_HALF_DOWN));
        System.out.println( new BigDecimal("13.336").setScale(2, BigDecimal.ROUND_HALF_DOWN));

        System.out.println( "---------------------------------------------------------------");

        System.out.println( new BigDecimal("13.334").setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println( new BigDecimal("13.335").setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println( new BigDecimal("13.336").setScale(2, BigDecimal.ROUND_HALF_UP));

        System.out.println( "---------------------------------------------------------------");
        BigDecimal decimal = new BigDecimal("10.2").subtract(new BigDecimal("10.2")).multiply(new BigDecimal("4")).multiply(new BigDecimal("1.5"));
        System.out.println( decimal);
        System.out.println(new BigDecimal("23.23").add(decimal));


    }
}
