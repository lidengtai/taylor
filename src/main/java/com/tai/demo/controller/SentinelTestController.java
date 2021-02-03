package com.tai.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: sentinel测试类
 * @author: toylor
 * @create: 2021-02-03 21:21
 **/
@RestController
@RequestMapping("/sentinel")
public class SentinelTestController {

    @GetMapping(value = "/test")
    public String hello() {
        return "Hello Sentinel";
    }
}
