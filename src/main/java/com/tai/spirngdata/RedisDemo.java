package com.tai.spirngdata;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Date;

/**
 * @description: spring-boot-data-redis
 * @author: Taylor
 * @date :  2021-04-15 11:34
 **/
public class RedisDemo {

    @Autowired
    private RedisTemplate redisTemplate;

    public void test(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.pop("setkey");

        HashOperations hashOperations = redisTemplate.opsForHash();

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();


    }

    @Test
    public void testTime(){
        for (int i = 0; i < 1000; i++) {

            Date nowtime = new Date();
            System.out.println(System.currentTimeMillis());
        }
    }
}
