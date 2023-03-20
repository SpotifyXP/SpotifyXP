package com.spotifyxp;


import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.dummy.DummyJFrame;
import com.spotifyxp.enums.LookAndFeel;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.LogManager;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.utils.GlobalLookAndFeel;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.panels.ContentPanel;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.io.File;
import java.util.Scanner;
import org.apache.logging.log4j.Level;

@SuppressWarnings("Convert2Lambda")
public class Initiator {
    static SpotifyAPI api = null;
    public static void main(String[] args) {
        if(new File("pom.xml").exists()) {
            args = new String[] {"--debug"};
        }
        if(args.length>0) {
            if(args[0].equals("--debug")) {
                PublicValues.debug = true;
            }
        }
        if(PublicValues.debug) {
            new LogManager().setLevel(Level.ALL);
        }else {
            new LogManager().setLevel(Level.OFF);
        }
        new SplashPanel().show();
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setAutoFindLanguage();
        PublicValues.logger.setColored(false);
        PublicValues.logger.setShowTime(false);
        //Read settings
        PublicValues.config = new Config();
        new GlobalLookAndFeel().setLookAndFeel(LookAndFeel.Graphite.getClassName());
        if(PublicValues.config.get(ConfigValues.username.name).equals("")) {
            new LoginDialog().open();
        }
        Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
        api = new SpotifyAPI();
        SpotifyAPI.Player player = new SpotifyAPI.Player(api);
        new KeyListener().start();
        new BackgroundService().start();
        PublicValues.elevated = new SpotifyAPI.OAuthPKCE();
        ContentPanel panel = new ContentPanel(player, api);
        panel.open();
        new Analytics();
        SplashPanel.hide();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        SpotifyHttpManager.triggerTokenExpire = true;
    }
}
