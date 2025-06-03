package org.example.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeviceStatusService {

    private StringRedisTemplate redisTemplate;

    /**
     * 裝置定時回報在線狀態，每次更新 TTL。
     * DeviceStatusService 提供定時心跳報告 設備每 30 秒呼叫一次
     */
    public void reportDeviceOnline(String deviceId) {
        String key = "device:" + deviceId + ":status";
        redisTemplate.opsForValue().set(key, "online", Duration.ofSeconds(30));
    }

    /**
     * 查詢裝置是否在線。
     */
    public boolean isDeviceOnline(String deviceId) {
        String key = "device:" + deviceId + ":status";
        return redisTemplate.hasKey(key);
    }
}
