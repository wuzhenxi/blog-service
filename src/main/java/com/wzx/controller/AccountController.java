package com.wzx.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzx.dto.LoginDTO;
import com.wzx.common.lang.Result;
import com.wzx.constants.MethodName;
import com.wzx.entity.User;
import com.wzx.service.UserService;
import com.wzx.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Slf4j
@Api(tags = "系统用户操作")
@RestController
public class AccountController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @ApiOperation(MethodName.USER_LOGIN)
    public Result login(@Validated @RequestBody LoginDTO loginDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        user.setLastLogin(LocalDateTime.now());
        userService.updateById(user);

        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .put("lastLogin", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(user.getLastLogin()))
                .map()
        );
    }

    // 只能老用户注册账号
    @RequiresAuthentication
    @PostMapping("/signup")
    @ApiOperation(MethodName.USER_SIGNUP)
    public Result signup(@Validated @RequestBody User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setStatus(1);
        userService.save(user);
        return Result.succ(user.getUsername());
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    @ApiOperation(MethodName.USER_LOGOU)
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

}
