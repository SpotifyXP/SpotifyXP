package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;

public class ConsoleMode implements Argument{
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> {
            new NoGUI().runArgument("").run();
            PublicValues.consoleMode = true;
            ConsoleLogging.killLogging();
            ConsoleLoggingModules.killLogging();
            System.out.println("Starting SpotifyXP! Please wait...");
        };
    }

    @Override
    public String getName() {
        return "consoleMode";
    }

    @Override
    public String getDescription() {
        return "Enables ConsoleMode (NoGUI)";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
