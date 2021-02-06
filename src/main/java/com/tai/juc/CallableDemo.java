package com.tai.juc;

import java.util.concurrent.*;

/**
 * @description: callable 接口 适配器模式 FutureTask callable RunnableFuture 继承 runnable 接口
 *              适配thread runnable接口
 * @author: Taylor
 * @create: 2021-02-06 21:38
 **/
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        new Thread(futureTask,"AA").start();

        //等待返回结果 超时设置
//        try {
//            Integer rusult2 = futureTask.get(3, TimeUnit.SECONDS);
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//        Integer rusult2 = futureTask.get();
        //写主线程的逻辑
        /**
         * futureTask.get()放在最后 否则会线程阻塞 妨碍主线的继续执行
         *
         */
        while(!futureTask.isDone()){
            Thread.yield();
        }


        int result1 = 100;
         System.out.println(Thread.currentThread().getName()+"############################");
        Integer rusult2 = futureTask.get();
        System.out.println(result1+rusult2);
    }
}

class MyThread implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {

         System.out.println(Thread.currentThread().getName()+" call begin  ");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2021;
    }
}
