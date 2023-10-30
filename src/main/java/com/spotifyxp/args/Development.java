package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class Development implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.devMode = true;
            }
        };
    }

    @Override
    public String getName() {
        return "devMode";
    }

    @Override
    public String getDescription() {
        return "Enable devMode";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
