package org.example.controller.app;

import org.example.dao.AccessRecordInterface;
import org.example.model.AccessRecord;
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
    private AccessRecordInterface accessRecordDao;

    @Autowired
    private AccessSocketPusher accessSocketPusher;

    /**
     *  撈出史資料
     *  Spring WebSocket + STOMP
     *  STOMP 不走 HTTP GET/POST，因此不適用 /api/ 這類 API gateway 或 RESTful 前綴
     *  annotation @MessageMapping("/request-records")：前端要 send 到 /app/request-records
     *  convertAndSend("/topic/access", ...)：後端 push 給訂閱 /topic/access 的前端
     */
    @MessageMapping("/request-records")
    public void handleRequestRecords() {
        List<AccessRecord> records = accessRecordDao.findLatest();
        System.out.println("records:"+records);
        System.out.println("now:"+ LocalDateTime.now());
        accessSocketPusher.pushAccessRecord();
    }

    @GetMapping("/testPush")
    public void testPush() {
        AccessRecord ac = new AccessRecord();
        ac.setRecordUid(UUID.randomUUID().toString());
        ac.setAccessTime(LocalDateTime.now());
        ac.setReason("門禁測試通過");
        ac.setSuccessful(true);
        ac.setCardId("124444");
        ac.setDeviceId("DEVICE-001");
        accessRecordDao.insert(ac);
        accessSocketPusher.pushAccessRecord();
    }

}
