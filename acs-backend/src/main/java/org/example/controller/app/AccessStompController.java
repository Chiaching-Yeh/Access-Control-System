package org.example.controller.app;

import org.example.dao.AccessRecordInterface;
import org.example.model.AccessRecord;
import org.example.service.AccessRecordService;
import org.example.service.AccessSocketPusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class AccessStompController {

    @Autowired
    private AccessSocketPusher accessSocketPusher;

    @Autowired
    private AccessRecordService accessRecordService;

    /**
     *  撈出史資料
     *  Spring WebSocket + STOMP
     *  annotation @MessageMapping("/request-records")：前端要 send 到 /app/request-records
     *  convertAndSend("/topic/access", ...)：後端 push 給訂閱 /topic/access 的前端
     */
    @MessageMapping("/request-records")
    public void handleRequestRecords() {
        List<AccessRecord> records = accessRecordService.findLatest(5);
        System.out.println("records:"+records);
        System.out.println("now:"+ LocalDateTime.now());
        accessSocketPusher.pushAccessRecordList(records);
    }

}
