package com.spotifyxp.support;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.ContentPanel;

public class SteamDeckSupportModule {
    public SteamDeckSupportModule() {
        try {
            ContentPanel.frame.setUndecorated(true);
        }catch (Exception ignored) {
        }
        if(PublicValues.theme.hasLegacyUI()) {
            ContentPanel.frame.setJMenuBar(null);
            ContentPanel.steamDeck();
        }
        PublicValues.isLinux = true;
    }
}
