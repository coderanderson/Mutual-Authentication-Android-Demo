package com.example.mzong.mutual_auth_demo;


import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


import static android.content.ContentValues.TAG;

public class SecureHttpClient extends DefaultHttpClient {
    private int securePort;

    private static final String KEY_STORE_PASS = "123456";

    private static KeyStore truststore;

    private static KeyStore keystore;

    private static Context context;

    public SecureHttpClient(final int port, final Context context) {
        this.securePort = port;
        this.context = context;
    }

    private SSLSocketFactory createSSLSocketFactory() {
        Log.d(TAG, "Creating SSL socket factory");

        truststore = null;
        keystore = null;

        try {
            truststore = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.clienttruststore);
            truststore.load(in, KEY_STORE_PASS.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            keystore = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.client);
            keystore.load(in, KEY_STORE_PASS.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.createFactory(keystore, "123456", truststore);
    }

    private SSLSocketFactory createFactory(final KeyStore keystore,
                                           final String keystorePassword, final KeyStore truststore) {

        SSLSocketFactory factory;
        try {
            factory = new SSLSocketFactory(keystore, "123456", truststore);
            factory.setHostnameVerifier(
                    (X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            Log.e(TAG, "Caught exception when trying to create ssl socket factory. Reason: " +
                    e.getMessage());
            throw new RuntimeException(e);
        }

        return factory;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        Log.d(TAG, "Creating client connection manager");

        final SchemeRegistry registry = new SchemeRegistry();

        Log.d(TAG, "Adding https scheme for port " + securePort);
        registry.register(new Scheme("https", this.createSSLSocketFactory(), this.securePort));

        return new SingleClientConnManager(getParams(), registry);
    }
}
