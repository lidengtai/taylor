package com.tai.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程里的判断都是用while判断
 * if判断会出现虚假唤醒问题
 */
class ShareData{

    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment(){
        lock.lock();
        try{

//            if(number != 0){
            while (number != 0){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            number++;
            System.out.println(Thread.currentThread().getName()+" "+number);
            //唤醒所有
            condition.signalAll();
        }finally {
            lock.unlock();
        }

    }
    public void decrement(){
        lock.lock();
        try{
//            if (number == 0){
            while (number == 0){
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            number--;
            System.out.println(Thread.currentThread().getName()+" "+number);
            condition.signalAll();
        }finally{
            lock.unlock();
        }
    }
}
/**
 * @description: 生产者消费者 传统版
 *  初始值为0 一个线程加 1 一个线程减 1
 * @author: Taylor
 * @create: 2021-02-02 23:42
 **/
public class ProdConsumerTradition {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareData.increment();
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareData.decrement();
            }
        },"B").start();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareData.increment();
            }
        },"C").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                shareData.decrement();
            }
        },"D").start();
    }

}
