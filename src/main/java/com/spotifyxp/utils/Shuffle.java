package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;

import java.util.ArrayList;
import java.util.Collections;

public class Shuffle {
    public static ArrayList<String> before = new ArrayList<>();
    public static void makeShuffle() {
        try {
            ArrayList<String> mixed = new ArrayList<>();
            for(ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                mixed.add(t.getUri());
            }
            before = mixed;
            Collections.shuffle(mixed);
            PublicValues.spotifyplayer.tracks(true).next.clear();
            for(String s : mixed) {
                PublicValues.spotifyplayer.addToQueue(s);
            }
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
    }
}
