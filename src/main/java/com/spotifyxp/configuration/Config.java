package com.spotifyxp.configuration;

import com.spotifyxp.PublicValues;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.GraphicalMessage;
import oracle.sql.NUMBER;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Config {
    private static class JSONProperties extends JSONObject {
        public JSONProperties() {
        }

        public JSONProperties(String source) {
            super(source);
        }

        public void put(ConfigValues value) {
            put(value.name, value.defaultValue);
        }
    }
    JSONProperties properties;
    public Config() {
        properties = new JSONProperties();
        if(!new File(PublicValues.configfilepath).exists()) {
            properties.put(ConfigValues.facebook);
            properties.put(ConfigValues.audioquality);
            properties.put(ConfigValues.theme);
            properties.put(ConfigValues.disableplayerstats);
            properties.put(ConfigValues.showallrecommendations);
            properties.put(ConfigValues.username);
            properties.put(ConfigValues.lastfmpassword);
            properties.put(ConfigValues.lastfmusername);
            properties.put(ConfigValues.lastfmtracklimit);
            properties.put(ConfigValues.lastfmartistlimit);
            properties.put(ConfigValues.mypalpath);
            properties.put(ConfigValues.password);
            properties.put(ConfigValues.hideExceptions);
            properties.put(ConfigValues.spconnect);
            properties.put(ConfigValues.language);
            properties.put(ConfigValues.webinteface);
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
                Files.write(Paths.get(PublicValues.configfilepath), properties.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                GraphicalMessage.sorryErrorExit("Failed creating important directory");
            }
        }
        try {
            properties = new JSONProperties(IOUtils.toString(Files.newInputStream(Paths.get(PublicValues.configfilepath)), Charset.defaultCharset()));
        } catch (IOException e) {
            GraphicalMessage.sorryErrorExit("Failed creating important directory");
        }
    }

    public void checkConfig() {
        //Checks config for invalid values
        boolean foundInvalid = false;
        for(ConfigValues value : ConfigValues.values()) {
            //Handle some values that need extra checking
            switch (value.name) {
                case "settings.playback.quality":
                    if(!(ConfigValueTypes.parse(properties.get(value.name)) == value.type)) {
                        ConsoleLogging.warning("Key '" + value.name + "' has the wrong value type: '" + ConfigValueTypes.parse(properties.get(value.name)) + "'! Resetting to default value...");
                        properties.put(value.name, value.defaultValue);
                        foundInvalid = true;
                    }
                    try {
                        Quality.valueOf(properties.getString(value.name));
                    }catch (IllegalArgumentException e) {
                        ConsoleLogging.warning("Key '" + value.name + "' has an invalid value: '" + properties.get(value.name) + "'! Resetting to default value...");
                        properties.put(value.name, value.defaultValue);
                        foundInvalid = true;
                    }
                    break;
                case "settings.ui.theme":
                    if(!(ConfigValueTypes.parse(properties.get(value.name)) == value.type)) {
                        ConsoleLogging.warning("Key '" + value.name + "' has the wrong value type: '" + ConfigValueTypes.parse(properties.get(value.name)) + "'! Resetting to default value...");
                        properties.put(value.name, value.defaultValue);
                        foundInvalid = true;
                    }
                    PublicValues.themeLoader = new ThemeLoader();
                    if(!ThemeLoader.hasTheme(properties.getString(value.name))) {
                        ConsoleLogging.warning("Key '" + value.name + "' has an invalid value: '" + properties.get(value.name) + "'! Resetting to default value...");
                        properties.put(value.name, value.defaultValue);
                        foundInvalid = true;
                    }
                    break;
                default:
                    if(!properties.has(value.name)) {
                        try {
                            properties.put(Objects.requireNonNull(ConfigValues.get(value.name)));
                            ConsoleLogging.warning("Key '" + value.name + "' not found! Creating...");
                        }catch (NullPointerException e) {
                            ConsoleLogging.error("Failed creating key '" + value.name + "'!");
                        }
                        continue;
                    }
                    if(!(ConfigValueTypes.parse(properties.get(value.name)) == value.type)) {
                        ConsoleLogging.warning("Key '" + value.name + "' has the wrong value type: '" + ConfigValueTypes.parse(properties.get(value.name)) + "'! Resetting to default value...");
                        properties.put(value.name, value.defaultValue);
                        foundInvalid = true;
                    }
            }
        }
        if(foundInvalid) {
            try {
                Files.write(Paths.get(PublicValues.configfilepath), properties.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                GraphicalMessage.sorryErrorExit("Failed creating important directory");
            }
        }
    }

    /**
     * Writes a new entry with the name and value to the config file
     * @param name Name of entry
     * @param value Value of entry
     */
    public void write(String name, Object value) {
        properties.put(name, value);
        try {
            Files.write(Paths.get(PublicValues.configfilepath), properties.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            GraphicalMessage.sorryErrorExit("Failed creating important directory");
        }
    }

    /**
     * Returns the value of the given entry inside the config
     * @param name name of the entry
     * @return value of given entry
     */
    public Object getObject(String name) {
        Object ret = properties.get(name);
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    public String getString(String name) {
        String ret = properties.getString(name);
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    public Boolean getBoolean(String name) {
        return properties.optBoolean(name);
    }

    public int getInt(String name) {
        return properties.optInt(name);
    }

    public Double getDouble(String name) {
        return properties.optDouble(name);
    }

    public Float getFloat(String name) {
        return properties.getFloat(name);
    }
}
