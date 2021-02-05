package com.wzx.controller;

import com.wzx.redis.publish.MessagePub;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis 发布订阅.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2021/2/3</pre>
 */
@Api(tags = "redis发布消息")
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private MessagePub messagePub;

    @PostMapping(value = "/publish", produces = "text/html;charset=utf-8")
    public String sendMessage (@RequestParam("msg") String msg) {
        return messagePub.sendMessage(msg);
    }
}
