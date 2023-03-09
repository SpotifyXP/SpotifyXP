package com.spotifyxp;


import com.spotifyxp.dummy.DummyJFrame;
import com.spotifyxp.enums.LookAndFeel;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.utils.GlobalLookAndFeel;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;

@SuppressWarnings("Convert2Lambda")
public class Initiator {
    static SpotifyAPI api = null;
    public static void main(String[] args) {
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage("en"); //The german translation is sh*t but funny (Hot List = Heiße Liste)
        PublicValues.logger.setColored(false);
        PublicValues.logger.setShowTime(false);
        //Read settings
        PublicValues.config = new Config();
        new GlobalLookAndFeel().setLookAndFeel(LookAndFeel.Graphite.getClassName());
        if(PublicValues.config.get(ConfigValues.username.name).equals("")) {
            new LoginDialog().open();
        }
        //---
        try {
            if(!(PublicValues.config.get(ConfigValues.mypalpath.name)).equals("")) {
                PublicValues.elevated = new SpotifyAPI.OAuthPKCE();
            }
        }catch (RuntimeException exc) {
            JOptionPane.showConfirmDialog(new DummyJFrame(), "Please set the mypal path under Settings>Configuration", "Mypal path not set", JOptionPane.OK_CANCEL_OPTION);
        }
        Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
        api = new SpotifyAPI();
        SpotifyAPI.Player player;
        if(PublicValues.config.get(ConfigValues.mypalpath.name).equals("")) {
            JOptionPane.showConfirmDialog(null, "Please set the mypal path under Settings>Configuration", "Mypal path not set", JOptionPane.OK_CANCEL_OPTION);
        }
        player = new SpotifyAPI.Player(api);
        new KeyListener().start();
        new BackgroundService().start();
        ContentPanel panel = new ContentPanel(player, api);
        panel.open();
    }
}
