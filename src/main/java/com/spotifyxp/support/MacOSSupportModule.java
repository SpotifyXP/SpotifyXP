package com.spotifyxp.support;

import com.spotifyxp.PublicValues;

public class MacOSSupportModule {
    public MacOSSupportModule() {
        if(!PublicValues.customSaveDir) {
            PublicValues.fileslocation = System.getProperty("user.home") + "/SpotifyXP";
            PublicValues.appLocation = PublicValues.fileslocation + "/AppData";
            PublicValues.configfilepath = PublicValues.fileslocation + "/config.properties";
            PublicValues.tempPath = System.getProperty("java.io.tmpdir");
        }
    }
}
