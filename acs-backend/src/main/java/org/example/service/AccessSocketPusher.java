package org.example.service;

import org.example.dao.AccessRecordInterface;
import org.example.model.AccessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessSocketPusher {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AccessRecordService accessRecordService;

    public void pushAccessRecord() {
        List<AccessRecord> records = accessRecordService.findLatest();
        messagingTemplate.convertAndSend("/topic/access", records);
    }

}
