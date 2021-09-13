package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author:yxl
 **/

/*
* GET POST PUT DELETE
* 获得 写入  修改  删除
* */

@RestController
public class TestController {

    @Value("${test.hello:KKKLL}")
    private  String testHello;

    @Resource
    private TestService testService;

    //http://127.0.0.1:8880/hello

    @GetMapping("/hello")
    public String hello(){
        return  "hello  yxl yyds   !!!"+testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return  "hello   yyds   !!" +name;
    }


    @GetMapping("/test/list")
    public List<Test> list(){
        return  testService.list();
    }
}
