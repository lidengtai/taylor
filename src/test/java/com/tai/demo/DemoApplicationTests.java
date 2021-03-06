package com.tai.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    public void test(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.pop("setkey");

        HashOperations hashOperations = redisTemplate.opsForHash();

        ZSetOperations zSetOperations = redisTemplate.opsForZSet();


    }
	@Test
	void contextLoads() {
	}

}
