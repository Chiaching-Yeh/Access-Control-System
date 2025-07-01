package org.example.configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLSocketFactory;

@Configuration
public class MqttConfig extends BeanConfiguration {

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setUserName(mqttUsername);
        options.setPassword(mqttPassword.toCharArray());

        // 若 broker 是 ssl://，才設置 TLS
        if (mqttBroker.startsWith("ssl://")) {
            options.setSocketFactory(SSLSocketFactory.getDefault());
            System.out.println("使用:"+mqttBroker);
        }

        return options;
    }


    @Bean
    public MqttClient mqttClient(MqttConnectOptions options) throws MqttException {
        return new MqttClient(mqttBroker, mqttClientId);
    }

}




