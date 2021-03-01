package com.tai.juc;

import java.util.concurrent.TimeUnit;

class HoldLock implements Runnable{
    private String lockA;
    private String lockB;

    public HoldLock(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+ "持有锁 "+ lockA +" 尝试获取 "+lockB);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+"持有锁 "+ lockA +" 尝试获取 "+lockB);
            }
        }
    }
}
/**
 * @description: 死锁
 *                  死锁是指两个或者两个以上的进程执行过程中，
 *                  因争夺资源而造成的一种互相等待的现象
 *
 *                *****************故障排查：检查排查是否死锁的相关命令*****************
 *                jps -l 查出是否有一直执行的进程 查出对应的进程号
 *                jstack 进程号 查看详情
 *
 * @author: Taylor
 * @create: 2021-02-08 22:20
 **/
public class DeadLockDemo {

    public static void main(String[] args) {

        String thread1= "lock1";
        String thread2 = "lock2";

        new Thread(new HoldLock(thread1,thread2),"A").start();

        new Thread(new HoldLock(thread2,thread1),"B").start();
    }
}
