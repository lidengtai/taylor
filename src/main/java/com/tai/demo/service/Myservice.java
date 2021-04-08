package com.tai.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tai.demo.dao.MyDao;
import com.tai.demo.domain.Book;

import javax.annotation.Resource;

/**
 * @description: service
 * @author: Taylor
 * @date :  2021-03-17 08:46
 *
 **/
public class Myservice {

    @Resource
    private MyDao myDao;

    void mytest(){
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>();

//        queryWrapper.between().le().groupBy()

    }
}
