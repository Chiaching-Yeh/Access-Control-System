package org.example.controller.app;

import lombok.extern.slf4j.Slf4j;
import org.example.configuration.BeanConfiguration;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.service.MqttAccessControlService;
import org.example.support.QrCodeSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/qr")
public class QrCodeController extends BeanConfiguration {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MqttAccessControlService mqttAccessControlService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AuthService authService;


    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateQrCode(@RequestBody Map<String, String> data) {

        log.info("收到請求 data: {}", data);

        String userId = data.get("userId");

        // 查是否有這個人
        Optional<User> userOpt = authService.findByUserId(userId);
        if (userOpt.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("code", "USER_NOT_FOUND");
            error.put("message", "此人不存在");
            return ResponseEntity.ok(error);
        }

        String uuid = UUID.randomUUID().toString();
        String redisKey = "qr:" + uuid;
        int expireSeconds = 60;

        // 1. 將 userId 與 uuid 對應存入 Redis，設定 60 秒有效時間
        redisTemplate.opsForValue().set(redisKey, userId, Duration.ofSeconds(expireSeconds));

        // 2. 將 uuid 構造可掃描的 URL
        System.out.println("qr apiPath>>"+apiPath);
        String qrContent = apiPath + "/api/qr/scan?uuid=" + uuid + "&deviceId=device-002";

        // 不要額外加 data:image/png;base64，確保乾淨
        String base64Qr = QrCodeSupport.generateBase64Qr(qrContent).replace("data:image/png;base64,", "");
        System.out.println("base64Qr>>"+base64Qr);

        // 3. 回傳 base64 QR 給前端
        Map<String, Object> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("qrCodeBase64", base64Qr);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/scan")
    public ResponseEntity<String> simulateQrScan(
            @RequestParam String uuid,
            @RequestParam String deviceId) {

        try {
            mqttAccessControlService.publishQrScan(uuid, deviceId);
            return ResponseEntity.ok("已模擬掃碼並送出 MQTT 訊息");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("模擬失敗: " + e.getMessage());
        }
    }

}
