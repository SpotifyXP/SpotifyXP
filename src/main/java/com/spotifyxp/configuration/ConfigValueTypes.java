package com.spotifyxp.configuration;

import java.util.NoSuchElementException;

/**
 * Holds all available config value types
 */
public enum ConfigValueTypes {
    STRING,
    INT,
    BOOLEAN,
    CUSTOM;

    public static ConfigValueTypes parse(Object object) {
        if (object instanceof String) {
            return ConfigValueTypes.STRING;
        } else if (object instanceof Integer) {
            return ConfigValueTypes.INT;
        } else if (object instanceof Boolean) {
            return ConfigValueTypes.BOOLEAN;
        } else if (ICustomConfigValue.class.isAssignableFrom(object.getClass())) {
            return ConfigValueTypes.CUSTOM;
        }
        throw new NoSuchElementException(object.toString());
    }
}
