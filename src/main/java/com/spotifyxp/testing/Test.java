package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.betaui.BETAUIService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.com.spotify.metadata.Metadata;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lib.libBrowser;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.panels.SettingsPanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Token;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) throws Exception {
        //new BETAUIService();
        //JFrame frame = new JFrame("Test");
        //libBrowser browser = new libBrowser("http://127.0.0.1:6969/app.html", frame);
        //frame.getContentPane().add(browser.getComponent());
        //frame.setPreferredSize(new Dimension(800, 600));
        //frame.setVisible(true);
        //frame.pack();
        SettingsPanel.triggerUninstall();
    }
}
