package org.example.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
public final class LineMessagingClientSupport {

    public static URI createUri(String host, String port, String path) {
        if (StringUtils.isBlank(port)) {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .scheme("https")
                    .host(host)
                    .path(path)
                    .build()
                    .toUri();
        } else {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .scheme("https")
                    .host(host)
                    .port(port)
                    .path(path)
                    .build()
                    .toUri();
        }
    }

    public static URI createUri(String host, String port, String path, String parameter, String value) {
        if (StringUtils.isBlank(port)) {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .scheme("https")
                    .host(host)
                    .path(path)
                    .queryParam(parameter, value)
                    .build()
                    .toUri();
        } else {
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .scheme("https")
                    .host(host)
                    .port(port)
                    .path(path)
                    .queryParam(parameter, value)
                    .build()
                    .toUri();
        }
    }



}
