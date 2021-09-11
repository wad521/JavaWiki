package com.jiawa.wiki.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author:yxl
 **/

/*
* GET POST PUT DELETE
* 获得 写入  修改  删除
* */

@RestController
public class TestController {
    //http://127.0.0.1:8880/hello

    @GetMapping("/hello")
    public String hello(){
        return  "hello  yxl yyds   !!!";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return  "hello   yyds   !!" +name;
    }
}
