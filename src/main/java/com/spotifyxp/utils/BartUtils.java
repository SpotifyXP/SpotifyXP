package com.spotifyxp.utils;

import com.spotifyxp.deps.se.michaelthelin.spotify.Base64;

public class BartUtils {
    public static String s(byte[] d) {
        return new String(Base64.decode(new String(d)));
    }

    public static String r(String t) {
        return Base64.encode(t.getBytes());
    }
}
