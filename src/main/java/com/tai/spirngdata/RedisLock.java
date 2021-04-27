package com.tai.spirngdata;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author taylor
 * @Description: TODO
 * @date 2021/4/27 21:10
 */
@Slf4j
public class RedisLock {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Redisson redisson;

    public static final String REDIS_LOCK_KEY = "lock:taylor:001";


    /**
     * 单机环境下  使用Synchronized或lock来实现
     * 但是在分布式环境下 因为竞争的线程可能不在同一个节点(不是同一个JVM) 所以许需要一个让所有进程都能访问到的锁来实现
     * 比如redis或者zookeeper来实现
     * <p>
     * 不同进程jvm层面的锁就不管用了，那么可以利用第三方的一个组件，来获取锁，未获取到锁，则阻塞当前想要运行的线程
     */
    public void demo2() {
        Lock lock = new ReentrantLock();
        if (lock.tryLock()) {
            log.info("加锁成功");
            sellGoods();
        } else {
            log.info("加锁失败");
        }
    }

    /**
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * Any previous time to live associated with the key is discarded on successful SET operation.
     * <p>
     * redis官网：https://redis.io/commands/set
     * The SET command supports a set of options that modify its behavior:
     * <p>
     * EX seconds -- Set the specified expire time, in seconds.
     * PX milliseconds -- Set the specified expire time, in milliseconds.
     * NX -- Only set the key if it does not already exist.
     * XX -- Only set the key if it already exist.
     * KEEPTTL -- Retain the time to live associated with the key.
     * <p>
     * 问题如果部署的微服务jar包的机器挂了，代码没有走到finllay里面释放锁，
     * 那么这个key就没有被删除 永远留在了redis服务器种  所有需要加一个过期时间
     */
    public void demo3() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        //setnx
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value);
        if (!aBoolean) {
            log.info("抢锁失败");
        } else {
            try {
                sellGoods();
            } finally {
                //释放锁
                redisTemplate.delete(REDIS_LOCK_KEY);
            }
        }
    }

    /**
     * 业务逻辑伪代码 ：商品买卖 超买超卖的问题
     */
    private void sellGoods() {

    }

    /**
     * setIfAbsent 和 expire 两个命令非原子性的
     * 所以需要使用一条命令
     */
    public void demo4() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        //setnx
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value);
        redisTemplate.expire(REDIS_LOCK_KEY, 10L, TimeUnit.SECONDS);
        if (!aBoolean) {
            log.info("抢锁失败");
        } else {
            try {
                sellGoods();
            } finally {
                //释放锁
                redisTemplate.delete(REDIS_LOCK_KEY);
            }
        }
    }

    /**
     * 这个时候感觉没有问题了
     * 如果业务线程A处理业务时间太长 redis的lock过期被删除
     * 线程B进入 此时线程A处理完成 删除了线程B的锁
     * 只能删除自己创建的锁 修改见demo6
     */
    public void demo5() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        //setnx
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value, 10L, TimeUnit.MINUTES);
        if (!aBoolean) {
            log.info("抢锁失败");
        } else {
            try {
                sellGoods();
            } finally {
                //释放锁
                redisTemplate.delete(REDIS_LOCK_KEY);
            }
        }
    }

    /**
     * 这个时候还有一个问题：
     * finally种 redis的get和delete非原子性的
     * 解决方案：1.使用Lua脚本
     * 2.用redis事务
     */
    public void demo6() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        //setnx
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value, 10L, TimeUnit.MINUTES);
        if (!aBoolean) {
            log.info("抢锁失败");
        } else {
            try {
                sellGoods();
            } finally {
                //释放锁
                if (value.equalsIgnoreCase((String) redisTemplate.opsForValue().get(REDIS_LOCK_KEY))) {
                    redisTemplate.delete(REDIS_LOCK_KEY);
                }
            }
        }
    }

    /**
     * 使用redis事务释放锁
     * redis事务介绍
     * 1.Redis事务是通过 MULTI,EXEC,DISCARD,WATCH这四个命令完成的
     * redis的单个命令都是原子性的 所有这个确保事务性的对象是命令集合
     * redis将命令集合序列化变确保处一事务的命令集合连续且不被打断的执行
     * redis不支持事务的回滚操作
     * <p>
     * :0>multi
     * "OK"
     * :0>set key1 aa
     * "QUEUED"
     * :0>set key2 cc
     * "QUEUED"
     * :0>exec
     * 1)  "OK"
     * 2)  "OK"
     * :0>
     * <p>
     * 如果 watch key1的话 在事务提交之前 另一个线程修改了 key1的值 那么
     * <p>
     * :0>watch key1
     * "OK"
     * :0>multi
     * "OK"
     * :0>set key1 aa    另一个线程 执行了  set key1 cc
     * "QUEUED"
     * :0>set key2 bb
     * "QUEUED"
     * :0>EXEC
     * 执行完EXEC后没有结果返回
     */
    public void demo7() {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        //setnx
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value, 10L, TimeUnit.MINUTES);
        if (!aBoolean) {
            log.info("抢锁失败");
        } else {
            try {
                sellGoods();
            } finally {
                while (true) {
                    redisTemplate.watch(REDIS_LOCK_KEY);
                    if (value.equalsIgnoreCase((String) redisTemplate.opsForValue().get(REDIS_LOCK_KEY))) {
                        redisTemplate.setEnableTransactionSupport(true);
                        redisTemplate.multi();
                        redisTemplate.delete(REDIS_LOCK_KEY);
                        List exec = redisTemplate.exec();
                        if (exec == null) {
                            continue;
                        }
                    }
                    redisTemplate.unwatch();
                    break;
                }
            }
        }
    }

    /**
     * 使用lua脚本释放锁
     * 到目前为止 存在的问题：
     * 1.如果设置的过期时间比业务处理时间短 或者遇到连接超时等问题  key如何续期问题
     * 2.redis集群情况下 redis集群是 CAP理论种的 AP 高可用和分区容错性  redis异步复制的时候 主节点有锁key 这个时候从节点还没有复制这个锁key 主节点
     * 挂了 从节点上位 那么这个时候就没有这个锁key
     * 3 zookeeper 是CP 数据一致性和分区容错性
     * CAP理论不能同时满足 一般还是使用redis开发分布式锁
     * 续期问题可以使用 redisson
     */
    public void demo8() throws Exception {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        //setnx
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value, 10L, TimeUnit.MINUTES);
        if (!aBoolean) {
            log.info("抢锁失败");
        } else {
            try {
                sellGoods();
            } finally {
                Jedis jedis = new Jedis();

                String script = "if redis.call('get', KEYS[1]) == ARGV[1]" + "then "
                        + "return redis.call('del', KEYS[1])" + "else " + "  return 0 " + "end";
                try {
                    Object result = jedis.eval(script, Collections.singletonList(REDIS_LOCK_KEY), Collections.singletonList(value));
                    if ("1".equals(result.toString())) {
                        System.out.println("------del REDIS_LOCK_KEY success");
                    } else {
                        System.out.println("------del REDIS_LOCK_KEY error");
                    }
                } finally {
                    if (null != jedis) {
                        jedis.close();
                    }
                }
            }

        }
    }

    /**
     * redssion 默认30秒 watchdog 续期key
     */
    public void demo9() {
        RLock lock = redisson.getLock(REDIS_LOCK_KEY);
        try {
            lock.lock();
            if (lock.tryLock()) {
                log.info("加锁成功");
            }
        } finally {
            //如果直接unlock的话 有可能会抛出 IllegalMonitorStateException:attempt to unlock lock,not locked by current thread by node id:
            //原因：是在并发多的时候可能会遇到这种错误，可能会被重写抢占
//            lock.unlock();
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
