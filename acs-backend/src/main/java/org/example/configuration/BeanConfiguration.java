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

    @Value("${app.allowed-Origins.path}")
    protected String allowedOriginsPath;

    @Value("${app.QrCode.expireSeconds}")
    protected int expireSeconds;

    @Value("${mqtt.broker}")
    protected String mqttBroker;

    @Value("${mqtt.clientId}")
    protected String mqttClientId;

    @Value("${mqtt.username}")
    protected String mqttUsername;

    @Value("${mqtt.password}")
    protected String mqttPassword;

}
