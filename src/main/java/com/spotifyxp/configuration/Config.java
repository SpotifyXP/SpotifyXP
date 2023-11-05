package com.spotifyxp.configuration;

import com.spotifyxp.PublicValues;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.GraphicalMessage;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    JSONObject properties;
    public Config() {
        properties = new JSONObject();
        if(!new File(PublicValues.configfilepath).exists()) {
            properties.put(ConfigValues.facebook.name, false);
            properties.put(ConfigValues.audioquality.name, Quality.NORMAL.configValue());
            properties.put(ConfigValues.theme.name, "DARKGREEN");
            properties.put(ConfigValues.disableplayerstats.name, false);
            properties.put(ConfigValues.showallrecommendations.name, false);
            properties.put(ConfigValues.username.name, "");
            properties.put(ConfigValues.lastfmpassword.name, "");
            properties.put(ConfigValues.lastfmusername.name, "");
            properties.put(ConfigValues.lastfmtracklimit.name, 20);
            properties.put(ConfigValues.lastfmartistlimit.name, 10);
            properties.put(ConfigValues.mypalpath.name, "");
            properties.put(ConfigValues.password.name, "");
            properties.put(ConfigValues.hideExceptions.name, false);
            properties.put(ConfigValues.spconnect.name, false);
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
                Files.write(Paths.get(PublicValues.configfilepath), properties.toString().getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                GraphicalMessage.sorryErrorExit("Failed creating important directory");
            }
        }
        try {
            properties = new JSONObject(IOUtils.toString(Files.newInputStream(Paths.get(PublicValues.configfilepath))));
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
                    new ThemeLoader();
                    if(!ThemeLoader.hasTheme(properties.getString(value.name))) {
                        ConsoleLogging.warning("Key '" + value.name + "' has an invalid value: '" + properties.get(value.name) + "'! Resetting to default value...");
                        properties.put(value.name, value.defaultValue);
                        foundInvalid = true;
                    }
                    break;
                default:
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
    public void write(String name, String value) {
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
