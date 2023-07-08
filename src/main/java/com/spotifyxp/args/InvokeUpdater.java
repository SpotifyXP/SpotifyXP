package com.spotifyxp.args;

import com.spotifyxp.updater.Updater;

public class InvokeUpdater implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                new Updater().invoke();
                System.exit(0);
            }
        };
    }

    @Override
    public String getName() {
        return "invokeUpdater";
    }

    @Override
    public String getDescription() {
        return "Invokes the SpotifyXP Updater";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
