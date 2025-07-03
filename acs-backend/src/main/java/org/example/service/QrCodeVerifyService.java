package org.example.service;

import org.example.dao.AccessRecordInterface;
import org.example.dao.UserInterface;
import org.example.model.AccessRecord;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class QrCodeVerifyService {

    @Autowired
    private AccessRecordInterface accessRecordDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccessSocketPusher accessSocketPusher;

    @Autowired
    private AuthService authService ;

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
        record.setRecordUid(UUID.randomUUID().toString());
        String cardId = null;

        Optional<User> userOptional = authService.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getCardId() != null){
                cardId = user.getCardId();
            } else {
                cardId = "";
            }
        } else {
            System.out.println("使用者不存在");
        }

        record.setCardId(cardId);
        record.setUserId(userId);
        record.setDeviceId(deviceId);
        record.setAccessTime(LocalDateTime.now());

        if (userId != null) {
            redisTemplate.delete(key);
            record.setSuccessful(true);
            accessRecordDao.insert(record);
            // 新增推播：有成功的授權記錄，推給前端
            accessSocketPusher.pushAccessRecord(record);
            return Optional.of(userId);
        } else {
            record.setSuccessful(false);
            record.setReason("使用者不存在");
            accessRecordDao.insert(record);
            // 新增推播：失敗也要推播前端記錄
            accessSocketPusher.pushAccessRecord(record);

            return Optional.empty();
        }


    }
}
