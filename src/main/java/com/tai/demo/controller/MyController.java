package com.tai.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tai.demo.domain.Book;

@RestController
public class MyController {
	
	@Autowired
	private Book book;

	
	@RequestMapping(value = "/book",method = RequestMethod.GET)
	public String index() {
		return "作者："+book.getAuthor()+"  书名："+book.getName()+" 作者年龄："+book.getAge();
	}
}
