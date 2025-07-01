package org.example.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class BeanConfiguration {

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

    @PostConstruct
    public void test(){
        System.out.println("=== MQTT 設定檢查 ===");
        System.out.println("mqtt.broker   = " + mqttBroker);
        System.out.println("mqtt.clientId = " + mqttClientId);
        System.out.println("mqtt.username = " + mqttUsername);
        System.out.println("mqtt.password = " + mqttPassword);
    }

}
