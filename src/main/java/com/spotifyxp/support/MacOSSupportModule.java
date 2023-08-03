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
        PublicValues.fileslocation = System.getProperty("user.home")+"/SpotifyXP";
        PublicValues.appLocation = PublicValues.fileslocation + "/AppData";
        PublicValues.configfilepath = PublicValues.fileslocation + "/config.properties";
        PublicValues.tempPath = System.getProperty("java.io.tmpdir");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SpotifyXP");
        PublicValues.isMacOS = true;
    }

    public static void iconSupport() {
        try {
            Class util = Class.forName("com.apple.eawt.Application");
            Method getApplication = util.getMethod("getApplication", new Class[0]);
            Object application = getApplication.invoke(util);
            Class params[] = new Class[1];
            params[0] = Image.class;
            Method setDockIconImage = util.getMethod("setDockIconImage", params);
            URL url = Initiator.class.getClassLoader().getResource("spotifyxp.png");
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            setDockIconImage.invoke(application, image);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
