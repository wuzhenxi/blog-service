package com.wzx.controller;


import cn.hutool.crypto.SecureUtil;
import com.wzx.common.lang.Result;
import com.wzx.entity.User;
import com.wzx.service.UserService;
import java.time.LocalDateTime;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresAuthentication
    @GetMapping("/index")
    public Result index() {
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @RequiresAuthentication
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setStatus(1);
        return Result.succ(user);
    }

}
