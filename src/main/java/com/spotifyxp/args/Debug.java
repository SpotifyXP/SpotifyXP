package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class Debug implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> PublicValues.debug = true;
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getDescription() {
        return "Enables Debugging";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
