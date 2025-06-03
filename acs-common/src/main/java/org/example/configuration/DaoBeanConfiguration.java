package org.example.configuration;


import lombok.extern.slf4j.Slf4j;
import org.example.dao.AccessRecordInterface;
import org.example.dao.UserInterface;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DaoBeanConfiguration {

    @Bean
    public UserInterface userDao(Jdbi jdbi) {
        return jdbi.onDemand(UserInterface.class);
    }

    @Bean
    public AccessRecordInterface accessRecordDao(Jdbi jdbi) {
        return jdbi.onDemand(AccessRecordInterface.class);
    }
}
