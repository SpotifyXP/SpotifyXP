package com.spotifyxp;


import com.spotifyxp.bypass.PortBypass;
import com.spotifyxp.detection.DetectFirewall;
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

public class Initiator {
    static SpotifyAPI api = null;
    static boolean skipConnection = false;
    public static void main(String[] args) {
        if(args.length>1) {
            String proxyip = args[0];
            String proxyport = args[1];
            new PortBypass(proxyip, proxyport);
        }else {
            if (new DetectFirewall().hasFirewall()) {
                JOptionPane.showConfirmDialog(null, "SpotifyXP has detected that some ports are blocked! This can lead to malfunctioning of SpotifyXP! Please pass this arguments: <proxyip> <proxyport> INFO: The proxy needs to be http", "Port Blocking", JOptionPane.OK_CANCEL_OPTION);
                System.exit(ExitCodes.PORT_BLOCKING_DETECTED.getCode());
            }
        }
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
        SpotifyAPI.Player player = null;
        player = new SpotifyAPI.Player(api);
        new KeyListener().start();
        new BackgroundService().start();
        ContentPanel panel = new ContentPanel(player, api);
        panel.open();
    }
}
