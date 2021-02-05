package com.tai.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource{
    //默认开启 标识
    private volatile boolean FLAG = true;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    BlockingQueue<String> blockingQueue = null;

    /**
     * 构造器传参 传接口 代码更加灵活
     * @param blockingQueue
     */
    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProduce() throws InterruptedException {
        String data = null;
        boolean ret;
        while (FLAG){
             data = atomicInteger.incrementAndGet()+"";
             ret = blockingQueue.offer(data,2, TimeUnit.SECONDS);
             if (ret){
                  System.out.println(Thread.currentThread().getName()+" 插入队列 "+data +" 成功");
             }else {
                  System.out.println(Thread.currentThread().getName()+" 插入队列 "+data +" 失败");
             }
             try {
                 TimeUnit.SECONDS.sleep(1);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
        }
         System.out.println(Thread.currentThread().getName()+"生产结束");
    }
    public void myConsume() throws InterruptedException {
        String data = null;
        while (FLAG){
            data = blockingQueue.poll(2, TimeUnit.SECONDS);
            if (data == null || data.equalsIgnoreCase("")){
                FLAG = false;
                System.out.println(Thread.currentThread().getName()+" 2秒没有消费到数据，消费退出");
                return;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName()+" 消费成功" +data);
        }
    }

    public void stop(){
        this.FLAG=false;
    }
}
/**
 * @description: 阻塞队列版的生产者消费者案例
 * @author: Taylor
 * @create: 2021-02-05 22:55
 **/
public class ProdConsumerBlockQueue {
    public static void main(String[] args) {

        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        new Thread(()->{
            try {
                myResource.myProduce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"produce ").start();

        new Thread(()->{
            try {
                myResource.myConsume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"consume ").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" 5秒后 生产被叫停");

        myResource.stop();
    }
}
