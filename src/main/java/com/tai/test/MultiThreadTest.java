package com.tai.test;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: 多线程测试
 * @author: Taylor
 * @date :  2020-10-12 09:07
 **/
public class MultiThreadTest {

    private static final int MAX_THREAD_NUM = 5;

    public static void main(String[] args){

        try {
            fixedThread();
//            dynamicThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList getList(){
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            idList.add(i);
        }
        return (ArrayList) idList;
    }
    /**
     * 线程数固定方式
     * @throws InterruptedException
     */
    public static void fixedThread() throws InterruptedException {

        ArrayList idList = getList();

        int threadNum = MAX_THREAD_NUM;
        if( idList.size() < MAX_THREAD_NUM){
            threadNum = idList.size();
        }
        System.out.println("开启的总线程数 :"+threadNum);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        //除数的整数
        int intNum = idList.size()/MAX_THREAD_NUM;
        //取模余数大小
        int remainderNum = idList.size()%MAX_THREAD_NUM;
        int initNum = remainderNum*(intNum+1);
        for (int i = 0; i < threadNum; i++) {
            MultiThread thread = new MultiThread();
            if( idList.size() < MAX_THREAD_NUM){
                thread.setIdList(idList.subList(i,i+1));
            }else{
                if(i < remainderNum ){
                    thread.setIdList(idList.subList(i * (intNum+1), (i + 1) * (intNum+1)));
                    System.out.println( "第 "+(i+1)+ "循环，处理任务数："+(intNum+1));
                }else {
                    thread.setIdList(idList.subList(initNum+(i-remainderNum)*intNum, initNum+(i-remainderNum+1)*intNum));
                    System.out.println("第 "+(i+1)+ "循环，处理任务数："+intNum);
                }
            }
            thread.setCountDownLatch(countDownLatch);
            executorService.submit(thread);
        }
        countDownLatch.await();
        executorService.shutdown();
    }

    /**
     * 线程数动态计算方式
     * @throws InterruptedException
     */
    public static void dynamicThread() throws InterruptedException {

        ArrayList idList = getList();

        int threadNum = MAX_THREAD_NUM;
        if( idList.size() < MAX_THREAD_NUM){
            threadNum = idList.size();
        }
        int perSize = 0;
        if(idList.size() % threadNum == 0){
            perSize = idList.size() / threadNum;
        }else{
            perSize = idList.size() / threadNum +1;
        }
        threadNum =(idList.size()-1)/perSize+1;

        System.out.println("开启的总线程数 :"+threadNum);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        System.out.println("每个线程最多处理的数据:"+perSize);
        for (int i = 0; i < threadNum; i++) {
            MultiThread thread = new MultiThread();
            if( idList.size() < MAX_THREAD_NUM){
                thread.setIdList(idList.subList(i,i+1));
            }else{
                if((i + 1) * perSize>idList.size()){
                    thread.setIdList(idList.subList(i * perSize, idList.size()));
                }else {
                    thread.setIdList(idList.subList(i * perSize, (i + 1) * perSize));
                }
            }
            thread.setCountDownLatch(countDownLatch);
            executorService.submit(thread);
        }

        countDownLatch.await();
        executorService.shutdown();
    }


     static class MultiThread implements Runnable {
        private List<Integer> idList;

        private CountDownLatch countDownLatch;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() +" 线程开启........");
            try {
                Thread.sleep(1000);
                System.out.println(this.idList.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
            System.out.println(Thread.currentThread().getName() +" 线程结束........ 还有 "+countDownLatch.getCount()+" 线程");
        }

        public void setIdList(List<Integer> idList) {
            this.idList = idList;
        }

        public void setCountDownLatch(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

    }

}
