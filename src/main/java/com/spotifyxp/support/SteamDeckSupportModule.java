package com.spotifyxp.support;

import com.spotifyxp.panels.ContentPanel;

public class SteamDeckSupportModule {
    public SteamDeckSupportModule() {
        try {
            ContentPanel.frame.setUndecorated(true);
        }catch (Exception ignored) {
        }
        ContentPanel.frame.setJMenuBar(null);
        ContentPanel.steamDeck();
    }
}
