package com.tai.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: synchronized lock 区别
 * 1 构成
 *      synchronized属于jvm层面的关键字（monitorenter monitorexit 底层是monitor对象完成）
 *      Lock 是juc下的具体的类
 * 2 使用方法
 *      synchronized不需要手动释放锁 执行完之后自动释放
 *      Lock主动释放 否则出现死锁
 * 3 等待是否可中断
 *      synchronized不可中断 除非异常或者正常运行完成
 *      ReentrantLock 可中断 设置超时 或者代码段中调用interrupt 方法
 * 4 加锁是否公平
 *      synchronized非公平锁
 *      ReentrantLock两者都可以  默认非公平锁
 * 5 锁绑定多个条件
 *      synchronized 要么唤醒一个 要么全部
 *      ReentrantLock 可以实现精确唤醒
 *
 * @author: Taylor
 * @create: 2021-02-03 23:32
 **/
public class SynchronizedAndLockAndLockSupport {


    public static void main(String[] args) {

//        synchronizedWaitNotify ();
//        conditionAwaitSignal();

        lockSupportParkUnpark();

//        manyCondition();
    }

    private static void lockSupportParkUnpark() {
        Thread aaa = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName()+" 被唤醒");

        },"AAA");

        aaa.start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
                LockSupport.unpark(aaa);
                System.out.println(Thread.currentThread().getName()+" 信号量通知");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"BBB").start();
    }

    private static void synchronizedWaitNotify() {
        Object o = new Object();
        new Thread(()->{
            synchronized (o){
                try {
                    System.out.println(Thread.currentThread().getName()+" come in ");
                    o.wait();
                    System.out.println(Thread.currentThread().getName()+" 被唤醒 ");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();
        new Thread(()->{
            synchronized (o){
                o.notify();
                System.out.println(Thread.currentThread().getName()+" 通知");
            }
        },"B").start();
    }

    private static void conditionAwaitSignal() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() +" come in");
                condition.await();

                System.out.println(Thread.currentThread().getName()+" 被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        },"AA").start();

        new Thread(()->{
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
                condition.signal();
                System.out.println(Thread.currentThread().getName()+" 通知");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }


        },"BB").start();
    }

    /**
     * 优势：多条件精准唤醒
     */
    private static void manyCondition() {

        PrintMethod printMethod = new PrintMethod();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {
                printMethod.print5();
            }
        },"aa").start();


        new Thread(()->{
            for (int i = 0; i < 5; i++) {

                printMethod.print10();
            }
        },"bb").start();

        new Thread(()->{
            for (int i = 0; i < 5; i++) {

                printMethod.print15();
            }
        },"cc").start();

    }

}
class PrintMethod{
    int number = 1;
    private Lock lock = new ReentrantLock();
    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    public void print5(){
        lock.lock();
        try{
            while (number != 1 ){
                conditionA.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()+" : "+i);
            }

            number=2;
            conditionB.signal();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print10(){
        lock.lock();
        try{
            while (number != 2 ){
                conditionB.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName()+" : "+i);
            }
            number=3;
            conditionC.signal();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print15(){
        lock.lock();
        try{
            while (number != 3 ){
                conditionC.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName()+" : "+i);
            }
            number=1;
            conditionA.signal();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}