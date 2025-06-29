package org.example.service;

import org.example.dao.AccessRecordInterface;
import org.example.model.AccessRecord;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessRecordService {

    @Autowired
    private AccessRecordInterface accessRecordDao;

    @Transaction
    public List<AccessRecord> findLatest(int limit) {
        return accessRecordDao.findLatest(limit);
    }

    @Transaction
    public void insert(AccessRecord accessRecord) {
        accessRecordDao.insert(accessRecord);
    }


}
