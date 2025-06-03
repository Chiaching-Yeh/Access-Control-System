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

    @MessageMapping("/request-records")
    public void handleRequestRecords() {
        List<AccessRecord> records = accessRecordDao.findAll();
        messagingTemplate.convertAndSend("/topic/access", records);
    }

}
