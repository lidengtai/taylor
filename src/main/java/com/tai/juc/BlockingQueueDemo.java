package com.tai.juc;

import java.util.concurrent.*;

/**
 * @description: 阻塞队列
 * LinkedBlockingQueue:有链表构成的有界阻塞队列 默认值接近无界 （Integer.MAX_VALUE)）
 * ArrayBlockingQueue：有数组构成的有界阻塞队列
 * SynchronousQueue：不存储元素的阻塞队列 即单个元素队列
 * ---------------------------------
 * PriorityBlockingQueue：支持优先级排序的无解阻塞队列
 * DelayQueue：使用优先级队列实现的延迟无界阻塞对列
 * LinkedTransferQueue：由链表组成的无界阻塞对列
 * LinkedBlockingDeque：由链表组成的双向阻塞对列
 *
 * @author: Taylor
 * @create: 2021-02-01 22:52
 **/
public class BlockingQueueDemo {

    public static void main(String[] args) {
//        arrayBlockQueueDemo();
        sychronousQueueDemo();
    }

    public static void sychronousQueueDemo() {
        /**
         * 不存储消息 生产一个消费一个  消费之前下一个消息只能阻塞等待消费
         */
        SynchronousQueue<Object> synchronousQueue = new SynchronousQueue<>();
        new Thread(()->{
            try {
                System.out.println(Thread.currentThread().getName()+" put a");
                synchronousQueue.put("a");

                System.out.println(Thread.currentThread().getName()+" put b");
                synchronousQueue.put("b");

                System.out.println(Thread.currentThread().getName()+" put c");
                synchronousQueue.put("c");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A线程 ").start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"take "+synchronousQueue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"take "+synchronousQueue.take());

                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName()+"take "+synchronousQueue.take());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B线程 ").start();
    }


    public static void arrayBlockQueueDemo() {
        /**
         * 1 抛出异常 add remove
         *      添加元素超出对列长度抛出：java.lang.IllegalStateException: Queue full
         *      取出元素超出对列长度抛出：java.util.NoSuchElementException
         */
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(3);

        System.out.println("--------add remove---------");
        System.out.println(arrayBlockingQueue.add("a"));
        System.out.println(arrayBlockingQueue.add("b"));
        System.out.println(arrayBlockingQueue.add("c"));
//        System.out.println(arrayBlockingQueue.add("d"));

        //取出元素先进先出
        System.out.println(arrayBlockingQueue.element());

        System.out.println(arrayBlockingQueue.remove());
        System.out.println(arrayBlockingQueue.remove());
        System.out.println(arrayBlockingQueue.remove());
//        System.out.println(arrayBlockingQueue.remove());

        System.out.println("--------offer poll---------");
        /**
         * 2 offer poll
         *      添加元素超出对列长度 返回false
         *      取出元素超出对列长度 取值null
         */
        System.out.println(arrayBlockingQueue.offer("a"));
        System.out.println(arrayBlockingQueue.offer("b"));
        System.out.println(arrayBlockingQueue.offer("c"));
//        System.out.println(arrayBlockingQueue.offer("d"));

        //检查元素
        System.out.println(arrayBlockingQueue.peek());

        System.out.println(arrayBlockingQueue.poll());
        System.out.println(arrayBlockingQueue.poll());
        System.out.println(arrayBlockingQueue.poll());
//        System.out.println(arrayBlockingQueue.poll());

        System.out.println("--------offer poll- timeout'--------");
        /**
         * 2 offer poll 超时版本
         *      添加元素超出对列长度 超时返回false
         *      取出元素超出对列长度 取值null
         */
        try {
            System.out.println(arrayBlockingQueue.offer("a",2l, TimeUnit.SECONDS));
            System.out.println(arrayBlockingQueue.offer("b",2l, TimeUnit.SECONDS));
            System.out.println(arrayBlockingQueue.offer("c",2l, TimeUnit.SECONDS));
            System.out.println(arrayBlockingQueue.offer("d",2l, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("---------put take--------");

        /**
         * 1 优雅操作 put take
         *      添加元素超出对列长度  一直阻塞 直到有消息被消费腾出空间
         *      取出元素超出对列长度 一直阻塞  直到有消息被生产出来
         */
        ArrayBlockingQueue<String> arrayBlockingQueue1 = new ArrayBlockingQueue<>(3);
        try {
            arrayBlockingQueue1.put("a");
            arrayBlockingQueue1.put("b");
            arrayBlockingQueue1.put("c");
//            arrayBlockingQueue.put("d");

            System.out.println(arrayBlockingQueue1.take());
            System.out.println(arrayBlockingQueue1.take());
            System.out.println(arrayBlockingQueue1.take());
//            System.out.println(arrayBlockingQueue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
