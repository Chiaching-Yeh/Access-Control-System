package org.example.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
@Component
public class TimeLogger {

    @PostConstruct
    public void printTimezone() {
        System.out.println("ZoneId.systemDefault() = " + ZoneId.systemDefault());
        System.out.println("TimeZone.getDefault() = " + TimeZone.getDefault().getID());
    }

}
