package com.web.kokoro.backend.core.test.controller;

import com.web.kokoro.backend.base.Result;
import com.web.kokoro.backend.pojo.User;
import com.web.kokoro.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class HelloController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("Hello World ~");
        return "Hello World ~";
    }

    //springboot方式
    @RequestMapping("/simpleParam")
    public Result simpleParam(){
        return Result.success(userMapper.list());
    }
}
