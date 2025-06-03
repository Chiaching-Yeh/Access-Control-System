package org.example.controller.app;


import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.*;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.service.line.LineBotService;
import org.example.support.FunctionLogSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@LineMessageHandler
@RestController
public class LineMessageHandlerController {

    @Autowired
    LineBotService lineBotService;


    private TextMessage buildDefaultTextMessage() {
        String replayText = "您好~歡迎您寄信到「服務信箱」\n" +
                "（）\n" +
                "針對使用有任何疑問可連絡以下資訊：\n" +
                "客服專線：886-2-0000-0000\n" +
                "傳真：886-2-2000000\n" +
                "E-mail:charlieyeh1114@gmail.com";

        return new TextMessage.Builder(replayText)
                .build();
    }

    /**
     * MessageEvent
     * @param event
     * @throws Exception
     */
    @EventMapping
    public void handleTextMessageEvent(MessageEvent event) throws Exception {

        FunctionLogSupport.start("handleTextMessageEvent");

        final String originalMessageText = ((TextMessageContent) event.message()).text();
        handleTextContent(event, originalMessageText, event.replyToken());

        FunctionLogSupport.end("handleTextMessageEvent");

    }

    /**
     * followEvent
     * @param event
     * 在這裡可以執行保存 userId 的邏輯或進行其他處理
     */
    // 處理 Follow 事件
    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
//        String userId = event.getSource().getUserId();
        String userId = event.source().userId();
        User user = new User();
        log.info("User followed: {}", userId);
        user.setUserId(userId);
//        userService.insert(user);
        // 在這裡可以執行保存 userId 的邏輯或進行其他處理
    }


    private void handleTextContent(Event event, String content, String replyToken) throws Exception {

        final Source source = event.source();
        final String senderId = source.userId();

        log.info("source: {}", source);
        log.info("text: {}", content);
        log.info("senderId: {}", senderId);

        lineBotService.replyToUser(replyToken, content);

    }

    @EventMapping
    public void handleDefaultMessageEvent(MessageEvent event) {
        log.info("Received event: {}", event);
    }




}
