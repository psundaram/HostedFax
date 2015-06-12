package com.anpi.app.api.util;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

/**
 * Helper class to accept the self-signed certificates.
 */

public class FakeSSLSocketFactory extends SSLSocketFactory {

    /**
     * Instantiates a new fake ssl socket factory.
     *
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyManagementException the key management exception
     * @throws KeyStoreException the key store exception
     * @throws UnrecoverableKeyException the unrecoverable key exception
     */
    public FakeSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(trustStrategy, hostnameVerifier);
    }

    private static final X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {

		public boolean verify(String arg0, SSLSession arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		public void verify(String host, SSLSocket ssl) throws IOException {
			// TODO Auto-generated method stub
			
		}

		public void verify(String host, X509Certificate cert) throws SSLException {
			// TODO Auto-generated method stub
			
		}

		public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
			// TODO Auto-generated method stub
			
		}
        
    };

    private static final TrustStrategy trustStrategy = new TrustStrategy() {
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    };
}
