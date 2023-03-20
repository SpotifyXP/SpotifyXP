package com.spotifyxp.logging;


import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;

public class LogManager {
    public void setLevel(Level level) {
        Configurator.setRootLevel(level);
    }
}
