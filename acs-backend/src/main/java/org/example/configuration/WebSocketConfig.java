package org.example.configuration;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void checkWebSocketSupport() {
        System.out.println("=== WebSocket Support Check ===");
        try {
            // 1. 檢查類別是否存在
            Class.forName("jakarta.websocket.server.ServerContainer");
            Class.forName("org.apache.tomcat.websocket.server.WsServerContainer");
            System.out.println("All WebSocket classes loaded successfully");

            // 2. 檢查 ServletContext 中是否有 ServerContainer (這才是關鍵!)
            Object serverContainer = servletContext.getAttribute("jakarta.websocket.server.ServerContainer");
            if (serverContainer != null) {
                System.out.println(" ServerContainer properly initialized: " + serverContainer.getClass().getName());
            } else {
                System.out.println("ServerContainer NOT initialized in ServletContext");
            }

        } catch (Exception e) {
            System.err.println("WebSocket class loading failed: " + e.getMessage());
        }
    }

}
