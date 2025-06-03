package org.example.configuration;


import com.linecorp.bot.messaging.client.MessagingApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LineMessagingAPIClientConfiguration {

    // 使用 @Value 直接注入配置文件中的值
    @Value("${line.bot.channel-token}")
    private String channelToken;

    @Value("${line.bot.channel-secret}")
    private String channelSecret;

    @Value("${line.bot.userId}")
    private String userId;

    // 將 MessagingApiClient 作為 Spring Bean 進行初始化
    @Bean
    public MessagingApiClient messagingApiClient() {
        return MessagingApiClient.builder(channelToken).build();
    }

}
