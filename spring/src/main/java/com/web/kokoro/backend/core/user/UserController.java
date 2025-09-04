package com.web.kokoro.backend.core.user;

import com.web.kokoro.backend.base.Result;
import com.web.kokoro.backend.core.user.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    UserController(@Autowired UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/no")
    public Result no() {
        return Result.success("no");
    }

    @RequestMapping("/test")
    public Result test() {
        return Result.success("test");
    }

    //注册
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterRequest requestBody){
        return userService.register(requestBody);
    }

    //登录
    @RequestMapping("/login")
    public Result login(@RequestBody RegisterRequest requestBody){
        return userService.login(requestBody);
    }
}
