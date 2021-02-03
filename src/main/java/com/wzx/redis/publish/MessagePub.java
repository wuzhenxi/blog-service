package com.wzx.redis.publish;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2021/2/3</pre>
 */
@Slf4j
@Service
public class MessagePub {
    @Resource(name = "redisTemplateUtils")
    private RedisTemplate<String, Object> redisTemplate;

    public String sendMessage(String msg) {
        try {
            redisTemplate.convertAndSend("topic", msg);
            return "消息发送成功";
        } catch (Exception e) {
            log.error("redis sendMessage msg:{},err:{}", e.getMessage(), e);
            return "消息发送失败";
        }
    }
}
