package com.tai.juc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author taylor
 * @Description: TODO
 * @date 2021/4/18 15:19
 */
public class ThreadPoolWhile {


    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {

        System.out.println("cpu:"+Runtime.getRuntime().availableProcessors());
        int i = 1;
        while (true){
            Set<String> set = new HashSet();
            for (int j = 0; j < 10000; j++) {
                set.add("set-"+j);
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSSSSS");
            System.out.println(format.format(new Date())+" 开始执行第"+i+"次循环");
            if (set.size()>0){
                CountDownLatch countDownLatch = new CountDownLatch(set.size());
                for (String s : set) {
                    int finalI = i;
                    executorService.execute(()->{
                        //处理业务逻辑
                        try {
                            //随机生成1~5的整数
                            int num = (int) (Math.random() * 10 + 10);
                            TimeUnit.MILLISECONDS.sleep(num);
//                            System.out.println("s:"+s+"-"+ finalI);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally {
//                            System.out.println("第"+finalI+"次循环，还有"+countDownLatch.getCount()+"个线程在执行");
                            countDownLatch.countDown();
                            try {
                                TimeUnit.MILLISECONDS.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                try {
                    //整个循环如果1分钟还没执行完，继续下次循环
                    countDownLatch.await(1,TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                System.out.println(format.format(new Date())+" 准备执行第"+i+"次循环");
            }else {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
