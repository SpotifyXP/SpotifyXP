package com.spotifyxp.configuration;


import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@SuppressWarnings("CanBeFinal")
public class Config {
    Properties properties;
    public Config() {
        properties = new Properties();
        if(!new File(PublicValues.configfilepath).exists()) {
            properties.put(ConfigValues.sendanalytics.name, "true");
            properties.put(ConfigValues.audioquality.name, "NORMAL");
            properties.put(ConfigValues.theme.name, "DARK");
            properties.put(ConfigValues.disableplayerstats.name, "false");
            properties.put(ConfigValues.showallrecommendations.name, "false");
            properties.put(ConfigValues.username.name, "");
            properties.put(ConfigValues.mypalpath.name, "");
            properties.put(ConfigValues.password.name, "");
            properties.put(ConfigValues.hideExceptions.name, "false");
            properties.put(ConfigValues.language.name, "English");
            if(!new File(PublicValues.fileslocation).exists()) {
                if(!new File(PublicValues.fileslocation).mkdir()) {
                    ConsoleLogging.changeName("SpotifyAPI");
                    ConsoleLogging.error("Failed to create files");
                }
            }
            if(!new File(PublicValues.appLocation).exists()) {
                if(!new File(PublicValues.appLocation).mkdir()) {
                    ConsoleLogging.error("Failed to create app location");
                }
            }
            try {
                if(!new File(PublicValues.configfilepath).createNewFile()) {
                    ConsoleLogging.error(PublicValues.language.translate("configuration.error.failedcreateconfig"));
                }
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
            }
            try {
                properties.store(new FileWriter(PublicValues.configfilepath), "SpotifyXP Config");
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
            }
        }
        try {
            properties.load(Files.newInputStream(Paths.get(PublicValues.configfilepath)));
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
    }
    public void write(String name, String value) {
        properties.put(name, value);
        try {
            properties.store(new FileWriter(PublicValues.configfilepath), "SpotifyXP Configuration file");
        } catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
            ConsoleLogging.error(PublicValues.language.translate("configuration.error.writefail"));
        }
        try {
            properties.load(Files.newInputStream(Paths.get(PublicValues.configfilepath)));
        } catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
            ConsoleLogging.error(PublicValues.language.translate("configuration.error.loadfail"));
        }
    }
    public String get(String name) {
        String ret = properties.getProperty(name);
        if(ret==null) {
            ret = name;
        }
        return ret;
    }
}
