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
            PublicValues.fileslocation = "/home/" + System.getProperty("user.name") + "/SpotifyXP";
            PublicValues.appLocation = PublicValues.fileslocation + "/AppData";
            PublicValues.configfilepath = PublicValues.fileslocation + "/config.json";
            PublicValues.tempPath = "/tmp";
        }
    }
}
