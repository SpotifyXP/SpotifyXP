package com.spotifyxp.args;

import com.spotifyxp.panels.ContentPanel;

public class SteamDeck implements Argument {
    @Override
    public Runnable runArgument(String commands) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    ContentPanel.frame.setUndecorated(true);
                }catch (Exception ignored) {
                }
                ContentPanel.frame.setJMenuBar(null);
                ContentPanel.steamDeck();
            }
        };
    }

    @Override
    public String getName() {
        return "steamdeck";
    }

    @Override
    public String getDescription() {
        return "Enables SteamDeck mode";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
