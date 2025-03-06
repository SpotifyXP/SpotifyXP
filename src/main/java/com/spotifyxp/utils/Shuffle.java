package com.spotifyxp.utils;

import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;

import java.util.ArrayList;
import java.util.Collections;

public class Shuffle {
    public static ArrayList<String> before = new ArrayList<>();

    public static void makeShuffle() {
        try {
            ArrayList<String> mixed = new ArrayList<>();
            for (ContextTrackOuterClass.ContextTrack t : InstanceManager.getSpotifyPlayer().tracks(true).next) {
                mixed.add(t.getUri());
            }
            before = mixed;
            Collections.shuffle(mixed);
            InstanceManager.getSpotifyPlayer().tracks(true).next.clear();
            for (String s : mixed) {
                InstanceManager.getSpotifyPlayer().addToQueue(s);
            }
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
        }
    }
}
