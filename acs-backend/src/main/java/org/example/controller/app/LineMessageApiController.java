package org.example.controller.app;


import lombok.extern.slf4j.Slf4j;
import org.example.service.line.LineBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@Slf4j
@RestController
public class LineMessageApiController {

    @Autowired
    LineBotService lineBotService;

    @Value("${line.bot.userId}")
    private String userId;

    /**
     * 推送訊息給指定用戶
     *
     * @return 回傳推送結果
     */
    @GetMapping("/api/v1/pushTest")
    public String pushMessageToUser() {
        try {
            LocalDateTime current = LocalDateTime.now();
            lineBotService.pushMessageToUser("現在時間:" + current.toString(),userId);
            return "Push message sent successfully to user: " + userId;
        } catch (Exception e) {
            // 處理任何異常
            e.printStackTrace();
            return "Failed to send push message: " + e.getMessage();
        }

    }

}










