package org.example.service;

import org.example.dao.AccessRecordInterface;
import org.example.dao.UserInterface;
import org.example.model.AccessRecord;
import org.example.model.User;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserInterface userDao;

    @Autowired
    private AccessRecordService accessRecordService;

    @Autowired
    private AccessSocketPusher accessSocketPusher;


    @Transaction
    public Map<String, Object> checkAuthorization (String cardId, String deviceId){


        // 是否綁定使用者
        Optional<User> userOptional = existsByCardId(cardId);
        userOptional.ifPresentOrElse(
                user -> System.out.println("✅ 找到使用者：" + user),
                () -> System.out.println("❌ 查無此卡號對應的使用者")
        );
        String reason = userOptional
                .map(user -> user.getIsActive() ? null : "該人員已離職或停權")
                .orElse("卡號沒有綁定使用者");

        boolean isSuccessful = (reason == null);  // 沒有 reason 表示成功

        // 寫入刷卡紀錄
        AccessRecord record = new AccessRecord();
        record.setRecordUid(UUID.randomUUID().toString());
        record.setCardId(cardId);
        record.setDeviceId(deviceId);
        record.setAccessTime(LocalDateTime.now());
        record.setSuccessful(isSuccessful);
        if (!isSuccessful) {
            record.setReason(reason);
        }

        accessRecordService.insert(record);

        // 推播刷卡紀錄給前端
        accessSocketPusher.pushAccessRecord(record);

        Map<String, Object> result = new HashMap<>();
        result.put("isSuccessful", isSuccessful);
        result.put("reason", reason);

        return result;

    }

    /**
     * 判斷是否授權卡號（存在 = true，不存在 = false）
     */
    public Optional<User> existsByCardId(String cardId) {
        return userDao.findByCardID(cardId);
    }

    public Optional<User> findByUserId(String userId) {
        return userDao.findByUserId(userId);
    }

}
