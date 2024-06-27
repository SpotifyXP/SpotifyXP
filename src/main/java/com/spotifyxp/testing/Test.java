package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.swingextension.JFrame;

import java.io.File;

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
        SplashPanel.frame = new JFrame();
        new LoginDialog().open();
        System.out.println("After");
    }
}
