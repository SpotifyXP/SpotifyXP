package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.logging.ConsoleLogging;

import java.io.IOException;

public class Token {
    public static String getDefaultToken() {
        try {
            return PublicValues.session.tokens().getToken("ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private".split(" ")).accessToken;
        }catch (IOException | MercuryClient.MercuryException e) {
            ConsoleLogging.Throwable(e);
            return getToken(0, "ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private".split(" "));
        }
    }

    private static String getToken(int times, String... scopes) {
        if(times > 5) {
            GraphicalMessage.sorryErrorExit("Couldn't get token. Tried it 5 times tho");
        }
        int newTimes = times;
        newTimes++;
        try {
            return PublicValues.session.tokens().getToken(scopes).accessToken;
        }catch (IOException | MercuryClient.MercuryException e) {
            ConsoleLogging.Throwable(e);
            return getToken(newTimes, scopes);
        }
    }

    public static String getToken(String... scopes) {
        try {
            return PublicValues.session.tokens().getToken(scopes).accessToken;
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            return getToken(0, scopes);
        }
    }
}
