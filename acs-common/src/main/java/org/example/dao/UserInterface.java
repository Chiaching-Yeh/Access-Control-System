package org.example.dao;


import org.example.model.User;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

@RegisterBeanMapper(User.class)
public interface UserInterface extends SqlObject {

    @SqlQuery("SELECT * FROM USERS WHERE cardId = ? ")
    Optional<User> findByCardID(String cardId);

    @SqlQuery("SELECT * FROM USERS WHERE userId = ? ")
    Optional<User> findByUserId(String userId);

    @SqlUpdate("INSERT INTO users (userId, name, cardId, isActive, updatedAt, createdAt) " +
            "VALUES (:userId, :name, :cardId, :isActive, :updatedAt , :createdAt)")
    int insert(@BindBean User user);

}
