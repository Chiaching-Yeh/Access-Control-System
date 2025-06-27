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

        try {
            System.out.println("------------執行/set------------");
            redisTemplate.opsForValue().set("testKey", "Spring Hello");
            return "Set OK";
        } catch (Exception e) {
            System.out.println("------------執行/set錯誤------------");
            e.printStackTrace();
            System.out.println("e.getClass().getName():"+e.getClass().getName());
            System.out.println("e.getMessage();>>"+e.getMessage());
            return "Redis Error: " + e.getClass().getName() + ": " + e.getMessage();
        }
    }

    @GetMapping("/get")
    public String getKey() {

        try {
            return redisTemplate.opsForValue().get("testKey");
        } catch (Exception e) {
            e.printStackTrace();
            return "Redis Error: " + e.getClass().getName() + ": " + e.getMessage();
        }
    }

}
