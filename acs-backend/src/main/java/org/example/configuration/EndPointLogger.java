package org.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@Component
public class EndPointLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping mapping;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        mapping.getHandlerMethods().forEach((key, value) ->
                System.out.println("[ 註冊的 endpoint: " + key + "]"));
    }

}
