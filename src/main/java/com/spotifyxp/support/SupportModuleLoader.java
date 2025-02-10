package com.spotifyxp.support;

import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;

import java.util.ArrayList;

public class SupportModuleLoader {
    private static final ArrayList<SupportModule> supportModules = new ArrayList<>();

    public SupportModuleLoader() {
        supportModules.add(new LinuxSupportModule());
        supportModules.add(new MacOSXSupportModule());
    }

    public void loadModules() {
        for (SupportModule module : supportModules) {
            if (module.getOSName().equals(System.getProperty("os.name"))) {
                module.run();
            }
        }
    }
}
