package com.jiawa.wiki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/hello")
    public String hello(){
        return  "hello  yxl yyds   !!!";
    }
}
