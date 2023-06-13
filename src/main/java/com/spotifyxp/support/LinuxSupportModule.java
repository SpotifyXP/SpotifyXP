package com.spotifyxp.support;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.GraphicalMessage;

public class LinuxSupportModule {
    public LinuxSupportModule() {
        PublicValues.fileslocation = "/home/" + System.getProperty("user.name") + "/SpotifyXP";
        PublicValues.appLocation = PublicValues.fileslocation + "/AppData";
        PublicValues.configfilepath = PublicValues.fileslocation + "/config.properties";
        PublicValues.tempPath = "/tmp";
    }
}
