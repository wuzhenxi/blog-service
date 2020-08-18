package com.wzx.controller;


import cn.hutool.crypto.SecureUtil;
import com.wzx.common.lang.Result;
import com.wzx.constants.MethodName;
import com.wzx.entity.User;
import com.wzx.service.UserService;
import com.wzx.util.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Api(tags = "用户操作")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresAuthentication
    @PostMapping("/update")
    @ApiOperation(MethodName.UPDATE_USER)
    public Result updateUser(@Validated @RequestBody User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setCreated(LocalDateTime.now());
        userService.save(user);
        return Result.succ(user);
    }

    @RequiresAuthentication
    @PostMapping("/delete/{id}")
    @ApiOperation(MethodName.DELETE_USER)
    public Result deleteUser(@PathVariable(name = "id") Long id) {
        Assert.notNull(id, "用户有误");
        Assert.isTrue(ShiroUtil.getProfile().getId().equals(id),"无权删除其他用户");
        User user = userService.getById(id);
        Assert.notNull(user, "用户不存在");
        user.setStatus(0);
        return Result.succ(userService.save(user));
    }

}
