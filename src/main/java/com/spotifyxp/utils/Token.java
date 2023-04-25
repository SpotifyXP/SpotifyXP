package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.logging.ConsoleLogging;

import java.io.IOException;

public class Token {
    public static String getDefaultToken() {
        try {
            return PublicValues.session.tokens().getToken("ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private".split(" ")).accessToken;
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.bug("Couldn't get default token");
            return "FAILED";
        }
    }
}
