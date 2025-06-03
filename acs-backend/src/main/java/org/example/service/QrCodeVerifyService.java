package org.example.service;

import org.example.dao.AccessRecordInterface;
import org.example.model.AccessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QrCodeVerifyService {

    @Autowired
    private AccessRecordInterface accessRecordDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 將使用者驗證資訊暫存於 Redis，TTL 為 180 秒（3 分鐘）。
     */
    public void cacheQrCode(String uuid, String userId) {
        redisTemplate.opsForValue().set("qr:" + uuid, userId, Duration.ofSeconds(180));
    }

    /**
     * 驗證掃碼是否有效，並視需要刪除 Redis Key（一次性用途）。
     */
    public Optional<String> verifyQrCode(String uuid, String deviceId) {
        String key = "qr:" + uuid;
        String userId = redisTemplate.opsForValue().get(key);

        AccessRecord record = new AccessRecord();
        record.setCardId(userId);
        record.setDeviceId(deviceId);
        record.setAccessTime(LocalDateTime.now());

        if (userId != null) {
            redisTemplate.delete(key);
            record.setSuccessful(true);
            accessRecordDao.insert(record);
            return Optional.of(userId);
        } else {
            record.setSuccessful(false);
            record.setReason("userId dose not exist");
            accessRecordDao.insert(record);
        }

        return Optional.empty();
    }
}
