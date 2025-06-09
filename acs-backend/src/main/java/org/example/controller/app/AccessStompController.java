package org.example.controller.app;

import org.example.dao.AccessRecordInterface;
import org.example.model.AccessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AccessStompController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AccessRecordInterface accessRecordDao;

    /**
     *  Spring WebSocket + STOMP
     *  STOMP 不走 HTTP GET/POST，因此不適用 /api/ 這類 API gateway 或 RESTful 前綴
     *  annotation @MessageMapping("/request-records")：前端要 send 到 /app/request-records
     *  convertAndSend("/topic/access", ...)：後端 push 給訂閱 /topic/access 的前端
     */

    @MessageMapping("/request-records")
    public void handleRequestRecords() {
        List<AccessRecord> records = accessRecordDao.findAll();
        messagingTemplate.convertAndSend("/topic/access", records);
    }

}
