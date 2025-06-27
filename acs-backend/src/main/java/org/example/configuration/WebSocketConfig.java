package org.example.configuration;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends BeanConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private ServletContext servletContext;

    /**
     * 控制哪些網域能成功「握手」建立 WebSocket 連線、註冊 STOMP WebSocket 端點，例如 /ws/access
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("----正在註冊 STOMP Endpoint /ws/access----");
        registry.addEndpoint("/ws/access").setAllowedOriginPatterns(allowedOriginsPath).withSockJS();
    }

    /**
     * 設定 STOMP 訊息代理的路由前綴
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }


}
