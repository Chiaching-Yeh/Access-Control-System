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


        boolean isSuccessful = false;
        String reason = null;

        Optional<User> userOptional = existsByCardId(cardId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (Boolean.TRUE.equals(user.getIsActive())) {
                System.out.println("✅ 找到有效使用者：" + user);
                isSuccessful = true;
            } else {
                System.out.println("❌ 使用者存在但已停權/離職：" + user);
                reason = "該人員已離職或停權";
            }
        } else {
            System.out.println("❌ 查無此卡號對應的使用者");
            reason = "卡號沒有綁定使用者";
        }

        // 建立 AccessRecord（不重複）
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
        accessSocketPusher.pushAccessRecord(record);

        // 回傳結果 Map
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
