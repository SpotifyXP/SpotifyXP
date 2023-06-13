package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class SetupComplete implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.foundSetupArgument = true;
                System.out.println("Found setup argument => " + PublicValues.foundSetupArgument);
            }
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