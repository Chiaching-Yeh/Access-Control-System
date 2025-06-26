package org.example.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/set")
    public String setKey() {
        redisTemplate.opsForValue().set("testKey", "Spring Hello");
        return "Set OK";
    }

    @GetMapping("/get")
    public String getKey() {
        return redisTemplate.opsForValue().get("testKey");
    }

}
