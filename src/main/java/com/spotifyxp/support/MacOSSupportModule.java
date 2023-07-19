package com.spotifyxp.support;

import com.spotifyxp.PublicValues;

import java.io.File;

public class MacOSSupportModule {
    public MacOSSupportModule() {
        PublicValues.fileslocation = System.getProperty("user.home")+ File.separator + "Documents" + "/SpotifyXP";
        PublicValues.appLocation = PublicValues.fileslocation + "/AppData";
        PublicValues.configfilepath = PublicValues.fileslocation + "/config.properties";
        PublicValues.tempPath = System.getProperty("java.io.tmpdir");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SpotifyXP");
    }
}
