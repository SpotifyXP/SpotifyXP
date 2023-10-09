package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class NoGUI implements Argument {

    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.nogui = true;
            }
        };
    }

    @Override
    public String getName() {
        return "no-gui";
    }

    @Override
    public String getDescription() {
        return "Runns SpotifyXP in headless mode";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
