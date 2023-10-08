package com.spotifyxp.configuration;


import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.GraphicalMessage;

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
            properties.put(ConfigValues.facebook.name, "false");
            properties.put(ConfigValues.sendanalytics.name, "true");
            properties.put(ConfigValues.audioquality.name, "NORMAL");
            properties.put(ConfigValues.theme.name, "DarkGreen");
            properties.put(ConfigValues.disableplayerstats.name, "false");
            properties.put(ConfigValues.showallrecommendations.name, "false");
            properties.put(ConfigValues.username.name, "");
            properties.put(ConfigValues.lastfmpassword.name, "");
            properties.put(ConfigValues.lastfmusername.name, "");
            properties.put(ConfigValues.lastfmtracklimit.name, "20");
            properties.put(ConfigValues.lastfmartistlimit.name, "10");
            properties.put(ConfigValues.mypalpath.name, "");
            properties.put(ConfigValues.password.name, "");
            properties.put(ConfigValues.hideExceptions.name, "false");
            properties.put(ConfigValues.language.name, "English");
            if(!new File(PublicValues.fileslocation).exists()) {
                if(!new File(PublicValues.fileslocation).mkdir()) {
                    GraphicalMessage.sorryErrorExit("Failed creating important directory");
                }
            }
            if(!new File(PublicValues.appLocation).exists()) {
                if(!new File(PublicValues.appLocation).mkdir()) {
                    GraphicalMessage.sorryErrorExit("Failed creating important directory");
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
                GraphicalMessage.sorryErrorExit("Failed creating important directory");
            }
        }
        try {
            properties.load(Files.newInputStream(Paths.get(PublicValues.configfilepath)));
        } catch (IOException e) {
            GraphicalMessage.sorryErrorExit("Failed creating important directory");
        }
    }

    /**
     * Writes a new entry with the name and value to the config file
     * @param name
     * @param value
     */
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

    /**
     * Returns the value of the given entry inside the config
     * @param name name of the entry
     * @return value of given entry
     */
    public String get(String name) {
        String ret = properties.getProperty(name);
        if(ret==null) {
            ret = "";
        }
        return ret;
    }
}
