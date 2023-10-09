package com.spotifyxp.support;

import com.spotifyxp.Initiator;
import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

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
