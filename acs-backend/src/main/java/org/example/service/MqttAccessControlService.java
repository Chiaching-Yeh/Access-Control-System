package org.example.service;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.example.configuration.BeanConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class MqttAccessControlService extends BeanConfiguration {

    private static final String MQTT_CLIENT_ID = "java-backend";
    private static final String CARD_TOPIC = "door/request/card";
    private static final String QR_TOPIC = "door/request/qr";
    private static final String CARD_TOPIC_RESPONSE = "door/response/card";
    private static final String QR_TOPIC_RESPONSE = "door/response/qr";

    @Autowired
    private AuthService authService;

    @Autowired
    private QrCodeVerifyService qrCodeVerifyService;

    private MqttClient client;

    @PostConstruct
    public void init() {
        try {
            client = new MqttClient(mqttBroker, MQTT_CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("MQTT 連線中斷: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println("[MQTT] 收到訊息 from topic [" + topic + "]: " + payload);

                    if (topic.equals(CARD_TOPIC)) {
                        handleCardAuthorization(payload);
                    } else if (topic.equals(QR_TOPIC)) {
                        handleQrCodeAuthorization(payload);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("MQTT 訊息送達: " + token.isComplete());
                }
            });

            client.connect(options);
            client.subscribe(CARD_TOPIC);
            client.subscribe(QR_TOPIC);
            System.out.println("MQTT 已訂閱: " + CARD_TOPIC + " 與 " + QR_TOPIC);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * python CLI 模擬卡片
     * CARD_TOPIC_RESPONSE + "/" + deviceId 對刷卡的那一台設備做 response，不會造成其他裝置收到同一訊息
     * @param payload
     */
    private void handleCardAuthorization(String payload) {
        try {
            // 假設格式為 cardId:xxx,deviceId:yyy
            String[] parts = payload.split(",");
            String cardId = parts[0].replace("cardId:", "").trim();
            String deviceId = parts[1].replace("deviceId:", "").trim();

            boolean authorized = authService.checkAuthorization(cardId, deviceId);
            String responseTopic = CARD_TOPIC_RESPONSE + "/" + deviceId;
            String result = authorized ? "grant" : "deny";

            client.publish(responseTopic, new MqttMessage(result.getBytes()));
            System.out.println("[回應] 授權結果: " + result + " → topic: " + responseTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * python CLI 模擬 QR code
     * @param payload
     */
    private void handleQrCodeAuthorization(String payload) {
        try {
            // 假設格式為 uuid:xxxx,deviceId:yyy
            String[] parts = payload.split(",");
            String uuid = parts[0].replace("uuid:", "").trim();
            String deviceId = parts[1].replace("deviceId:", "").trim();

            Optional<String> userIdOpt = qrCodeVerifyService.verifyQrCode(uuid, deviceId);
            String result = userIdOpt.isPresent() ? "grant" : "deny";
            String responseTopic = QR_TOPIC_RESPONSE + "/" + deviceId;

            client.publish(responseTopic, new MqttMessage(result.getBytes()));
            System.out.println("[回應] QR驗證結果: " + result + " → topic: " + responseTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishQrScan(String uuid, String deviceId) {

        String payload = "uuid:" + uuid + ",deviceId:" + deviceId;
        System.out.println("publishQrScan Payload>>"+payload);

        try {

        MqttClient client = new MqttClient(mqttBroker, MqttClient.generateClientId());
        client.connect();
        client.publish(QR_TOPIC, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
        client.disconnect();
        System.out.println("[MQTT 發送] payload: " + payload);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}


