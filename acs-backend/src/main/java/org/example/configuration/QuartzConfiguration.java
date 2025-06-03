package org.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Configuration
public class QuartzConfiguration {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(springBeanJobFactory());
        return factory;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory();
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        return schedulerFactoryBean().getScheduler();
    }

}

