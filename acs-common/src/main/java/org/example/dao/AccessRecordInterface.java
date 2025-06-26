package org.example.dao;


import org.example.model.AccessRecord;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(AccessRecord.class)
public interface AccessRecordInterface extends SqlObject {

    @SqlUpdate("INSERT INTO accessRecord (cardId, accessTime, successful, reason, deviceId) " +
            "VALUES (:cardId, :accessTime, :successful ,:reason, :deviceId)")
    int insert(@BindBean AccessRecord record);

    @SqlQuery("SELECT * FROM accessRecord order by accessTime desc limit 1 ")
    List<AccessRecord> findLatest();

}
