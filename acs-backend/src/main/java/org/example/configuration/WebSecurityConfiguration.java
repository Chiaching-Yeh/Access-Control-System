package org.example.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.support.FunctionLogSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends BeanConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/static/**",
                "/userfiles/**",
                "/mp3/**"
        );
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        FunctionLogSupport.start("WebSecurityConfiguration.configure");

        http.sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .enableSessionUrlRewriting(false)
                .sessionFixation()
                .migrateSession()
        );

        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/ws/**").permitAll() // 明確允許 WebSocket 端點
                .requestMatchers("/ws/access/**").permitAll()
                .anyRequest()
                .permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);

        // 啟用 CORS
        http.cors(Customizer.withDefaults());

        // 打印所有的過濾器，確認 HeaderWriterFilter 是否在干擾過程中
        http.addFilterBefore(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                System.out.println("Before processing request: " + request.getRequestURI());
                filterChain.doFilter(request, response);
                System.out.println("After processing request: " + request.getRequestURI());
            }
        }, HeaderWriterFilter.class);

        FunctionLogSupport.end("WebSecurityConfiguration.configure");

        return http.build();

    }

}
