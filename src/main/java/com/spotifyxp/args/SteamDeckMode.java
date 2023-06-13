package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class SteamDeckMode implements Argument {

    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.isSteamDeckMode = true;
            }
        };
    }

    @Override
    public String getName() {
        return "enableSteamDeckMode";
    }

    @Override
    public String getDescription() {
        return "Enables SteamDeck Mode";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
