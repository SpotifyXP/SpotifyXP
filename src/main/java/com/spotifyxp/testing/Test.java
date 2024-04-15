package com.spotifyxp.testing;

import com.spotifyxp.Initiator;
import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.injector.InjectorAPI;
import com.spotifyxp.injector.InjectorStore;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.panels.SettingsPanel;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.theming.themes.CustomTheme;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        new InjectorStore().open();
    }
}
