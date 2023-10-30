package com.spotifyxp.args;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;

@SuppressWarnings("ConstantValue")
public class SetupComplete implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> {
            PublicValues.foundSetupArgument = true;
            ConsoleLogging.info("Found setup argument => " + PublicValues.foundSetupArgument);
        };
    }

    @Override
    public String getName() {
        return "setup-complete";
    }

    @Override
    public String getDescription() {
        return "Run SpotifyXP without Setup";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}