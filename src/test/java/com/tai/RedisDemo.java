package com.tai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Date;
import java.util.Set;

/**
 * @description: spring-boot-data-redis
 * @author: Taylor
 * @date :  2021-04-15 11:34
 **/
@SpringBootTest
public class RedisDemo {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();

        String key1="set:ldt:friends";
        String key2="set:ldt:follower";
        redisTemplate.delete(key1);
        redisTemplate.delete(key2);
        setOperations.add(key1,"郭德纲","高圆圆","于谦","梁咏琪","孙燕姿");
        setOperations.add(key2,"郭德纲","高圆圆","于谦","陈慧琳","许巍","赵雷");

        //取交集 共同好友
        Set<String> intersect = setOperations.intersect(key1, key2);
        for (String s : intersect) {
            System.out.println("共同好友："+s);
        }
        //取差集
        Set difference = setOperations.difference(key1, key2);
        for (Object o : difference) {
            System.out.println("差集："+o);
        }
        //取并集
        Set union = setOperations.union(key1, key2);
        for (Object o : union) {
            System.out.println("并集："+o);
        }



    }

    @Test
    public void testZSet(){

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        String setKey = "setKey:user";
//        redisTemplate.delete(setKey);
//        for (int i = 1; i <= 1000; i++) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(5);
//                long nowTime = System.currentTimeMillis();
//                zSetOperations.add(setKey,"ldt-"+i,Double.parseDouble(String.valueOf(i)));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        Set<String> rangeByScore = zSetOperations.rangeByScore(setKey, 1, 10, 0, 10);
        Set<ZSetOperations.TypedTuple<String>> byScoreWithScores = zSetOperations.rangeByScoreWithScores(setKey, 1, 10, 0, 10);

        for (Object o : rangeByScore) {
            System.out.println(o.toString());
        }

        System.out.println("---------------------------");

        for (ZSetOperations.TypedTuple<String> byScoreWithScore : byScoreWithScores) {
            System.out.println(byScoreWithScore.getScore()+"----->"+byScoreWithScore.getValue());
        }

    }

    @Test
    public void testTime(){
        for (int i = 0; i < 1000; i++) {

            Date nowtime = new Date();
            System.out.println(System.currentTimeMillis());
        }
    }
}
