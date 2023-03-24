package com.spotifyxp.designs;

public enum Theme {
    DARK("Dark"),
    LIGHT("Light");
    final String s;
    Theme(String toselect) {
        s = toselect;
    }
    String getAsString() {
        return s;
    }
}
