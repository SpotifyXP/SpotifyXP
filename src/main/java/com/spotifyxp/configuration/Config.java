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
            properties.put(ConfigValues.hideExceptions, "false");
            try {
                if(!new File(PublicValues.configfilepath).createNewFile()) {
                    ConsoleLogging.error(PublicValues.language.translate("configuration.error.failedcreateconfig"));
                }
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
                ConsoleLogging.error(PublicValues.language.translate("configuration.error.failedcreateconfig"));
            }
            try {
                properties.store(new FileWriter(PublicValues.configfilepath), PublicValues.language.translate("configuration.comment"));
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
                ConsoleLogging.error(PublicValues.language.translate("configuration.error.writefail"));
            }
        }
        try {
            properties.load(Files.newInputStream(Paths.get(PublicValues.configfilepath)));
        } catch (IOException e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
            ConsoleLogging.error(PublicValues.language.translate("configuration.error.loadfail"));
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
