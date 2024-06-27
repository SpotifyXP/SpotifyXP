package com.spotifyxp.support;

import com.spotifyxp.PublicValues;

public class LinuxSupportModule implements SupportModule {
    @Override
    public String getOSName() {
        return "Linux";
    }

    @Override
    public void run() {
        if(!PublicValues.customSaveDir) {
            PublicValues.fileslocation = System.getProperty("user.home") + "/SpotifyXP";
            PublicValues.appLocation = PublicValues.fileslocation + "/AppData";
            PublicValues.configfilepath = PublicValues.fileslocation + "/config.json";
            PublicValues.tempPath = System.getProperty("java.io.tmpdir");
        }
    }
}