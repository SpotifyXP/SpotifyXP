package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.exception.ElementNotFoundException;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.support.LinuxSupportModule;

import java.io.File;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ElementNotFoundException {
        new LinuxSupportModule().run();
        if(!new File(PublicValues.fileslocation).canWrite()) {
            System.err.println("Can't write to folder! Permission denied: " + PublicValues.fileslocation);
        }
    }
}
