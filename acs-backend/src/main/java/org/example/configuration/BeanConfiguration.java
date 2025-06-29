package org.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public abstract class BeanConfiguration {

    @Value("${app.qrApi.path}")
    protected String apiPath;

    @Value("${mqtt.broker}")
    protected String mqttBroker;

    @Value("${app.allowed-Origins.path}")
    protected String allowedOriginsPath;

    @Value("${app.QrCode.expireSeconds}")
    protected int expireSeconds;

}
