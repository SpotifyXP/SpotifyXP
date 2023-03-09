package com.spotifyxp.configuration;


import com.spotifyxp.PublicValues;
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
        if(!new File(PublicValues.fileslocation).exists()) {
            if(!new File(PublicValues.fileslocation).mkdir()) {
                ConsoleLogging.error("Failed to create files directory"); //ToDo: Translate
            }
        }
        if(!new File(PublicValues.configfilepath).exists()) {
            properties.put(ConfigValues.disableplayerstats.name, "false");
            properties.put(ConfigValues.showallrecommendations.name, "false");
            properties.put(ConfigValues.mypalpath.name, "");
            properties.put(ConfigValues.username.name, "");
            properties.put(ConfigValues.password.name, "");
            try {
                if(!new File(PublicValues.configfilepath).createNewFile()) {
                    ConsoleLogging.error("Cant create configfile"); //ToDo: Translate
                }
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
                ConsoleLogging.error("Cant create configfile"); //ToDo: Translate
            }
            try {
                properties.store(new FileWriter(PublicValues.configfilepath), "SpotifyXP Configuration file");
            } catch (IOException e) {
                ConsoleLogging.Throwable(e);
                ConsoleLogging.error("Cant store configfile"); //ToDo: Translate
            }
        }
        try {
            properties.load(Files.newInputStream(Paths.get(PublicValues.configfilepath)));
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ConsoleLogging.error("Failed to load configfile"); //ToDo: Translate
        }
    }
    public void write(String name, String value) {
        properties.put(name, value);
        try {
            properties.store(new FileWriter(PublicValues.configfilepath), "SpotifyXP Configuration file");
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ConsoleLogging.error("Cant store configfile"); //ToDo: Translate
        }
        try {
            properties.load(Files.newInputStream(Paths.get(PublicValues.configfilepath)));
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ConsoleLogging.error("Failed to load configfile"); //ToDo: Translate
        }
    }
    public String get(String name) {
        return properties.getProperty(name);
    }
}
