package org.example.service.line;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

@Slf4j
@Service
public class LineBotService {

    @Autowired
    protected MessagingApiClient messagingApiClient;

    @Value("${line.bot.userId}")
    private String userId;

    /**
     * 回覆訊息給用戶
     *
     * @param replyToken  來自 LINE 平台的回覆 token
     * @param messageText 要發送的訊息內容
     */
    public void replyToUser(String replyToken, String messageText) {
        // 檢查參數是否為空
        if (replyToken == null || replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken cannot be null or empty");
        }
        if (messageText == null || messageText.isEmpty()) {
            throw new IllegalArgumentException("messageText cannot be null or empty");
        }

        // 創建訊息和回覆對象
        TextMessage textMessage = new TextMessage("你說了:"+messageText);
        List<Message> messages = List.of(textMessage);
        ReplyMessageRequest replyMessageRequest = new
                ReplyMessageRequest(replyToken, messages, false);

        // 使用 MessagingApiClient 發送回覆訊息
        messagingApiClient.replyMessage(replyMessageRequest)
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        // 處理錯誤情況，並記錄具體的錯誤信息
                        System.err.println("Error while sending message: " + throwable.getMessage());
                        throwable.printStackTrace();
                    } else {
                        // 成功發送後的響應
                        System.out.println("Successfully sent message: " + messageText);
                    }
                });
    }


    /**
     * 推送訊息給指定用戶
     *
     * @param messageText 要推送的文字訊息
     */
    public void pushMessageToUser(String messageText , String pushTo) {
        // 創建 TextMessage
        TextMessage textMessage = new TextMessage(messageText);
        List<Message> messages = List.of(textMessage);

        // 創建 PushMessageRequest
        PushMessageRequest pushMessageRequest = new PushMessageRequest(pushTo, messages, false,null );

        // 使用 MessagingApiClient 發送推送訊息，並包含 X-Line-Retry-Key
        UUID retryKey = UUID.randomUUID();
        messagingApiClient.pushMessage(retryKey, pushMessageRequest)
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        // 處理推送訊息過程中的錯誤
                        System.err.println("Error while sending push message: " + throwable.getMessage());
                    } else {
                        // 成功推送訊息
                        System.out.println("Successfully sent push message to user: " + pushTo);
                    }
                });

    }

}





