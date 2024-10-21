package com.spotifyxp.configuration;

import java.util.NoSuchElementException;

/**
 * Holds all available config value types
 */
public enum ConfigValueTypes {
    STRING,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN;

    public static ConfigValueTypes parse(Object object) {
        if (object instanceof Byte) {
            return ConfigValueTypes.BYTE;
        } else if (object instanceof String) {
            return ConfigValueTypes.STRING;
        } else if (object instanceof Short) {
            return ConfigValueTypes.SHORT;
        } else if (object instanceof Integer) {
            return ConfigValueTypes.INT;
        } else if (object instanceof Long) {
            return ConfigValueTypes.LONG;
        } else if (object instanceof Float) {
            return ConfigValueTypes.FLOAT;
        } else if (object instanceof Double) {
            return ConfigValueTypes.DOUBLE;
        } else if (object instanceof Boolean) {
            return ConfigValueTypes.BOOLEAN;
        }
        throw new NoSuchElementException(object.toString());
    }
}
