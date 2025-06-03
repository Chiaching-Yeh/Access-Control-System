package org.example.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public final class IPSupport {

    public static String getClientIp(final HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            if (remoteAddr.equals("0:0:0:0:0:0:0:1")) remoteAddr = "10.225.9.153";
        }

        return remoteAddr;
    }

}
