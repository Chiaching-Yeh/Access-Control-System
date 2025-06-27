package org.example.configuration;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Autowired
    private LettuceConnectionFactory factory;

    /**
     * 手動建立 Lettuce Redis Connection Factory，開啟 autoReconnect 與連線 timeout。
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("RedisConfig: 建立 RedisConnectionFactory，主機：" + redisHost + ":" + redisPort);

        // Redis 基本設定
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration(redisHost, redisPort);

        // 客戶端選項設定：開啟重連與 timeout
        ClientOptions clientOptions = ClientOptions.builder()
                .autoReconnect(true)
                .socketOptions(SocketOptions.builder()
                        .connectTimeout(Duration.ofSeconds(3))
                        .build())
                .build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofSeconds(5))
                .build();

        return new LettuceConnectionFactory(redisConf, clientConfig);
    }

    /**
     * 主動初始化底層 Redis 連線池
     * 明確告訴 Spring：我需要一個 StringRedisTemplate，而且要用這個 LettuceConnectionFactory。
     * 這樣會讓 Spring Boot 在啟動時，建立這個 Bean，並自動初始化底層的連線池。
     * @param factory
     * @return
     */
    @Bean
    public StringRedisTemplate redisTemplate(LettuceConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);

        // 設定 key 和 value 的序列化格式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    /**
     * 啟動後測試 Redis 是否可寫入與讀取
     */
    @PostConstruct
    public void testRedisStartup() {
        try {
            factory.getConnection().ping();  // 強制提早連線，避免Redis連線延遲初始化導致CONNECTION REFUSE
            System.out.println("Redis 測試連線成功：" + redisHost + ":" + redisPort);
        } catch (Exception e) {
            System.err.println("Redis 測試連線失敗：" + e.getMessage());
        }
    }

}

