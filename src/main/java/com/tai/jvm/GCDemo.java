package com.tai.jvm;

import java.util.concurrent.TimeUnit;

/**
 * JVM给了三种选择：串行收集器、并行收集器、并发收集器，但是串行收集器只适用于小数据量的情况，所以这里的选择主要针对并行收集器和并发收集器。
 * 默认情况下，JDK5.0以前都是使用串行收集器，如果想使用其他收集器需要在启动时加入相应参数。JDK5.0以后，JVM会根据当前系统配置进行判断。
 * ----------------------------------------------------------------------------------------------------------------------
 * jps -l 查看java进程
 * GC详细信息 -XX:+PrintGCDetails
 * 打印GC详细信息：jinfo -flag PrintGCDetails 进程号（11764）
 * 元空间信息：-XX:MetaspaceSize 默认21M 实际生产环境中这个参数要调整
 *查看元空间：jinfo -flag MetaspaceSize 11764
 * -XX:PrintFlagsInitial JVM初始化所有参数(java -XX:+PrintFlagsInitial -version)
 * -XX:PrintFlagsFinal JVM修改后的所有参数(java -XX:+PrintFlagsFinal -version) (:= 修改后的标识)
 * -Xms <==> -XX:InitialHeapSize 初始化堆内存（默认操作系统内存的1/64）
 * -Xmx <==> -XX:MaxHeapSize  最大内存（默认操作系统的1/4）
 * -Xms -Xmx 一般都要配成一样 ：java垃圾回收机制清理完堆区后不需要重新分隔计算堆区的大小
 * -Xmn: 堆中新生代初始及最大大小，如果需要进一步细化，初始化大小用-XX:NewSize，最大大小用-XX:MaxNewSize
 * -Xss <==> -XX:ThreadStackSize 设置单个线程的大小 默认 linux：1024k windows根据jvm的内存空间分配
 * -XX:PermSize=512 非堆区初始内存
 * -XX:MaxPermSize=1024 非堆区最大内存
 *
 * -XX:+PrintCommandLineFlags------------配置这个打印出如下信息（最后显示默认）
 *      -XX:InitialHeapSize=257798976 -XX:MaxHeapSize=4124783616 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers
 *      -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation
 *      -XX:+UseParallelGC 默认垃圾回收器
 * ----------------------------------------------------------------------------------------------------------------------
 * 常见配置汇总
 * 堆设置
     * -Xms:初始堆大小
     * -Xmx:最大堆大小
 * -Xms100M -Xmx100M
     * -XX:NewSize=n:设置年轻代大小
     * -XX:NewRatio=n:设置年轻代和年老代的比值。如:为3，表示年轻代与年老代比值为1：3，年轻代占整个年轻代年老代和的1/4
     * -XX:SurvivorRatio=n:年轻代中Eden区与两个Survivor区的比值。注意Survivor区有两个。
          如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
     * -XX:MaxPermSize=n:设置持久代大小
 * 收集器设置
     * -XX:+UseSerialGC:设置串行收集器
     * -XX:+UseParallelGC:设置并行收集器
     * -XX:+UseParalledlOldGC:设置并行年老代收集器
     * -XX:+UseConcMarkSweepGC:设置并发收集器
 * 垃圾回收统计信息
     * -XX:+PrintGC
     * -XX:+PrintGCDetails
     * -XX:+PrintGCTimeStamps
     * -Xloggc:filename
 * 并行收集器设置
     * -XX:ParallelGCThreads=n:设置并行收集器收集时使用的CPU数。并行收集线程数。
     * -XX:MaxGCPauseMillis=n:设置并行收集最大暂停时间
     * -XX:GCTimeRatio=n:设置垃圾回收时间占程序运行时间的百分比。公式为1/(1+n)
 * 并发收集器设置
     * -XX:+CMSIncrementalMode:设置为增量模式。适用于单CPU情况。
     * -XX:ParallelGCThreads=n:设置并发收集器年轻代收集方式为并行收集时，使用的CPU数。并行收集线程数。
 *
 * ----------------------------------------------------------------------------------------------------------------------
 *调优总结
 *
 * 年轻代大小选择
 * 响应时间优先的应用：尽可能设大，直到接近系统的最低响应时间限制（根据实际情况选择）。在此种情况下，年轻代收集发生的频率也是最小的。同时，减少到达年老代的对象。
 * 吞吐量优先的应用：尽可能的设置大，可能到达Gbit的程度。因为对响应时间没有要求，垃圾收集可以并行进行，一般适合8CPU以上的应用。
 * 年老代大小选择
 * 响应时间优先的应用：年老代使用并发收集器，所以其大小需要小心设置，一般要考虑并发会话率和会话持续时间等一些参数。
 * 如果堆设置小了，可以会造成内存碎片、高回收频率以及应用暂停而使用传统的标记清除方式；如果堆大了，则需要较长的收集时间。最优化的方案，一般需要参考以下数据获得：
 * 并发垃圾收集信息
 * 持久代并发收集次数
 * 传统GC信息
 * 花在年轻代和年老代回收上的时间比例
 * 减少年轻代和年老代花费的时间，一般会提高应用的效率
 * 吞吐量优先的应用：一般吞吐量优先的应用都有一个很大的年轻代和一个较小的年老代。原因是，这样可以尽可能回收掉大部分短期对象，
 * 减少中期的对象，而年老代尽存放长期存活对象。
 * 较小堆引起的碎片问题
 * 因为年老代的并发收集器使用标记、清除算法，所以不会对堆进行压缩。当收集器回收时，他会把相邻的空间进行合并，这样可以分配给较大的对象。
 * 但是，当堆空间较小时，运行一段时间以后，就会出现“碎片”，如果并发收集器找不到足够的空间，那么并发收集器将会停止，
 * 然后使用传统的标记、清除方式进行回收。如果出现“碎片”，可能需要进行如下配置：
 * -XX:+UseCMSCompactAtFullCollection：使用并发收集器时，开启对年老代的压缩。
 * -XX:CMSFullGCsBeforeCompaction=0：上面配置开启的情况下，这里设置多少次Full GC后，对年老代进行压缩
 */
public class GCDemo {
    public static void main(String[] args) {
        try {
//            System.gc();
            System.out.println("hello GC");
//            System.out.println(Runtime.getRuntime().totalMemory());
//            System.out.println(Runtime.getRuntime().maxMemory());
//            System.out.println(Runtime.getRuntime().freeMemory());

            /**
             * 启动配置 -XX:+PrintGCDetails
             * java.lang.OutOfMemoryError: Java heap space场景
             * oom 会打印详细信息 分析GC过程
            */
            new Thread(()->{
                while (true){
                    try {
                        byte[] bytes = new byte[1024*1024*10];
                        TimeUnit.MILLISECONDS.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            },"A").start();
            new Thread(()->{
                try {
                    byte[] bytes = new byte[1024*1024*10];
                    TimeUnit.MILLISECONDS.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"B").start();
//            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
