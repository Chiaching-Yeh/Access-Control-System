package org.example.service;

import org.example.dao.AccessRecordInterface;
import org.example.dao.UserInterface;
import org.example.model.AccessRecord;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInterface userDao;

    @Autowired
    private AccessRecordInterface accessRecordDao;

    @Transaction
    public boolean checkAuthorization (String cardId, String deviceId){

        String key = "auth:card:" + cardId;

        // Step 1: 嘗試從 Redis 快取中取得授權結果
        String cacheResult = redisTemplate.opsForValue().get(key);
        if (cacheResult != null) {
            return Boolean.parseBoolean(cacheResult); // "true" 或 "false"
        }

        // Step 2: 查詢資料庫（實際應查權限表或卡片綁定表）
        boolean result = existsByCardId(cardId);

        // 寫入刷卡紀錄
        AccessRecord record = new AccessRecord();
        record.setCardId(cardId);
        record.setDeviceId(deviceId);
        record.setAccessTime(LocalDateTime.now());
        record.setSuccessful(result);
        if (!result){
            record.setReason("userId doesnt exist");
        }

        accessRecordDao.insert(record);

        // Step 3: 寫入 Redis 快取，有效時間 5 分鐘
        redisTemplate.opsForValue().set(key, String.valueOf(result), Duration.ofMinutes(5));

        return result;

    }

    /**
     * 判斷是否授權卡號（存在 = true，不存在 = false）
     */
    public boolean existsByCardId(String cardId) {
        return userDao.findByCardID(cardId).isPresent();
    }

}
