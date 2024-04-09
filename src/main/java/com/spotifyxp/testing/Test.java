package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.panels.SettingsPanel;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.theming.themes.CustomTheme;
import com.spotifyxp.utils.Token;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        for(SpotifyXPEvents event : SpotifyXPEvents.values()) {
            Events.register(event.getName(), true);
        }
        new CustomSaveDir().runArgument(new File("data").getAbsolutePath()).run();
        PublicValues.language = new libLanguage();
        PublicValues.language.setNoAutoFindLanguage("en");
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.config = new Config();
        PublicValues.config.checkConfig();
        InstanceManager.getPlayer();
        new CustomTheme().initTheme();
        JFrame frame = new JFrame("Settings Alpha");
        SettingsPanel panel = new SettingsPanel();
        frame.getContentPane().add(panel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SettingsPanel.applySettings();
            }
        });
        frame.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()));
        frame.setVisible(true);
        frame.pack();
    }
}
