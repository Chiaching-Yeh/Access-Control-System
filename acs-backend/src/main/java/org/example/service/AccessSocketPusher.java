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

    public void pushAccessRecord(AccessRecord record) {
        messagingTemplate.convertAndSend("/topic/access", record);
    }

    public void pushAccessRecordList(List<AccessRecord> records) {
        messagingTemplate.convertAndSend("/topic/access", records);
    }

}
