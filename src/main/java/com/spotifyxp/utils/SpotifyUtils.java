package com.spotifyxp.utils;

import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Image;
import com.spotifyxp.logging.ConsoleLogging;

public class SpotifyUtils {
    public static Image getImageForSystem(Image[] images) {
        if(SystemUtils.getUsableRAMmb() < 512) {
            for(Image i : images) {
                if(i.getWidth() == 64) {
                    return i;
                }
            }
            ConsoleLogging.warning("Can't get the right image for the system ram! Using the default one");
            return images[0];
        }else{
            return images[0];
        }
    }
}
