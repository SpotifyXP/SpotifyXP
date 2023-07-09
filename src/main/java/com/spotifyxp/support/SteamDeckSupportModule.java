package com.spotifyxp.support;

import com.spotifyxp.PublicValues;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.theming.themes.Legacy;

public class SteamDeckSupportModule {
    public SteamDeckSupportModule() {
        ContentPanel.frame.setUndecorated(true);
        if(PublicValues.theme instanceof Legacy) {
            ContentPanel.frame.setJMenuBar(null);
        }
    }
}
