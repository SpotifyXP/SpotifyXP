package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.injector.InjectorAPI;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.ApplicationUtils;
import com.spotifyxp.utils.ConnectionUtils;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Token;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.File;
import java.io.IOException;

public class Test {
    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
    public static void main(String[] args) throws Exception {
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();
        PublicValues.config = new Config();
        new Events();
        for(SpotifyXPEvents s : SpotifyXPEvents.values()) {
            Events.register(s.getName(), true);
        }
        Factory.getPlayerUtils().buildPlayer();
        String token = Token.getDefaultToken();
        try {
            HttpGet get = new HttpGet("");
            get.setHeader("User-Agent", "Spotify/8.9.14 iOS/17.2.1 (iPhone13,4)");
            get.setHeader("Authorization", "Bearer " + token);
            get.setHeader("content-type", "application/json");
            get.setHeader("spotify-app-version", "application/json");
            get.setHeader("app-platform", "iOS");
            HttpClient client = getNewHttpClient();
            System.out.println(client.execute(get).getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpHost proxy = new HttpHost("127.0.0.1", 8080);
            DefaultProxyRoutePlanner planner = new DefaultProxyRoutePlanner(proxy);

            DefaultHttpClient client = new DefaultHttpClient(ccm, params);
            client.setRoutePlanner(planner);
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
    }

}
