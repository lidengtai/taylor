package com.tai.demo.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Administrator
 *
 */
public class WatchThread {
	
	/**
	 * 测试函数
	 * 
	 * @throws InterruptedException
	 */
	public void testThread() throws InterruptedException {
		
		//首先查出所有效标签
		int allNum = 26;//数据总量
		int num = 5; //单个线程5个标签
		
//		int threadNum =(int)Math.ceil(allNum/100);
		int threadNum =(allNum-1)/num+1;
		
		// 初始化countDown
		CountDownLatch threadSignal = new CountDownLatch(threadNum);
		// 创建固定长度的线程池
//		Executor executor = Executors.newFixedThreadPool(threadNum);
		//此处不可以用接口 需要使用Executor的实现类 ExecutorService  Executor未提供shutdown等方法
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		for (int i = 1; i  < threadNum+1; i++) { // 开threadNum个线程
			String tableName="ce_u_label_index";
			tableName+=i;
			Runnable task = new TestThread(threadSignal,tableName);
			// 执行
			executor.execute(task);
			
		}
		threadSignal.await(); // 等待所有子线程执行完
		//固定线程池执行完成后 将释放掉资源 退出主进程
		executor.shutdown();//并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
		// do work end
		//退出主进程
		System.out.println(Thread.currentThread().getName() + "+++++++结束.");
	}
 
	/**
	 * 测试函数
	 */
	public static void main(String[] args) throws InterruptedException {
		WatchThread test = new WatchThread();
		test.testThread();
	}
 
	/**
	 * 内部类
	 * 作为内部类的时候 有一个好处 就是可以直接引用给类的主对象的成员变量 
	 *
	 */
	private class TestThread implements Runnable {
		private CountDownLatch threadsSignal;
		private String tableName;
 
		
		public TestThread(CountDownLatch threadsSignal, String tableName) {
			super();
			this.threadsSignal = threadsSignal;
			this.tableName = tableName;
		}
 
		public void run() {
			System.out.println(Thread.currentThread().getName() + "开始..." + tableName);
			System.out.println("开始了线程：：：：" + threadsSignal.getCount());
		
			//核心处理逻辑
			try {
				/**
				 * 根据传过来的表名取查询插入该表的基本表
				 * Oracle表中配置了基本表和临时表的关系
				 * 
				 */
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //	用到成员变量name作为参数
			
			// 线程结束时计数器减1
			threadsSignal.countDown();//必须等核心处理逻辑处理完成后才可以减1
			System.out.println(Thread.currentThread().getName() + "结束. 还有"
					+ threadsSignal.getCount() + " 个线程");
		}
	}

}
