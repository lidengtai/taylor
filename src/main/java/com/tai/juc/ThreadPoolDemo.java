package com.tai.juc;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 线程池参数自定义配置
 * maximumPoolSize: cpu密集型 IO密集型
 *      1 CPU密集型：一般线程数=cpu数最合适，适合场景：运算较多、业务逻辑较复杂
 *      2 IO密集型：CPU核心数×2=线程数。当线程进行 I/O 操作 CPU 空闲时，启用其他线程继续使用 CPU，以提高 CPU 的使用率。
 *                 cpu核数/(1-阻塞系数) 阻塞系数： 0.8 ~ 0.9
 * @author: Taylor
 * @create: 2021-02-07 23:13
 **/
public class ThreadPoolDemo {


    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5,
//                Runtime.getRuntime().availableProcessors(),//cpu密集型
            (int) (Runtime.getRuntime().availableProcessors() *8),//io密集型
            3,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
//                new ThreadPoolExecutor.AbortPolicy()
//                new ThreadPoolExecutor.DiscardOldestPolicy()
//                new ThreadPoolExecutor.DiscardPolicy()
    );
    public static void main(String[] args) {

        System.out.println("cpu core :"+Runtime.getRuntime().availableProcessors());
        System.out.println("max :"+(Integer)Runtime.getRuntime().availableProcessors() *2);


        for (int i = 0; i < 50; i++) {
            int finalI = i;

            threadPoolExecutor.execute(()->{
                System.out.println(Thread.currentThread().getName()+"业务逻辑处理........" + finalI);

            });
        }
//        System.out.println(threadPoolExecutor.getActiveCount());

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("剩余多少个线程："+Thread.activeCount());
    }
}
