package org.example.support.okhttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AllowAllHostnameVerifier implements HostnameVerifier {

    public AllowAllHostnameVerifier() {
        super();
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

}
