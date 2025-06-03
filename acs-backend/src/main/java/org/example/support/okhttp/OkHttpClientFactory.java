package org.example.support.okhttp;

import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;

public final class OkHttpClientFactory {

    public static OkHttpClient create() {
        X509TrustManager trustManager = new TrustAllTrustManager();
        SSLSocketFactory sslSocketFactory;
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[] {trustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            sslSocketFactory = null;
        } catch (KeyManagementException e) {
            sslSocketFactory = null;
        }

        if (sslSocketFactory == null)
            throw new AssertionError("SSLSocketFactory create fail");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(new AllowAllHostnameVerifier())
                .connectTimeout(Duration.ofSeconds(15))
                .readTimeout(Duration.ofSeconds(90))
                .build();

        return client;

    }

    private static long secondsToMillis(long seconds) {
        return seconds * 1000;
    }

    private OkHttpClientFactory() {
        super();
    }

}
