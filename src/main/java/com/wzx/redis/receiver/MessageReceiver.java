package com.wzx.redis.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 接收消息
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2021/2/3</pre>
 */
@Slf4j
@Component
public class MessageReceiver implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        String msg= redisSerializer.deserialize(message.getBody());
        log.info("Received <" + msg + ">");
    }
}
