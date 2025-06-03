package org.example.support;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.SecureRandom;

public class TrustCertificateSupport {
    // trusting all certificate
    public void doTrustToCertificates() throws Exception {
      
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(
            null, 
            new TrustManager[]{
                new TrustManagerDelegateSupport(
                      (X509TrustManager)trustManagerFactory.getTrustManagers()[0],
                      (X509TrustManager)trustManagerFactory.getTrustManagers()[0]
                )
            }, 
            new SecureRandom()
        );
        
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        
    }
}
