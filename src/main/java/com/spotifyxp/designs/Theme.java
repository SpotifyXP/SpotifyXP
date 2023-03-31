package com.spotifyxp.designs;

public enum Theme {
    DARK("Dark"),
    LIGHT("Light"),
    WINDOWS("Windows"),
    MacOSDark("MacOSDark"),
    MacOSLight("MacOSLight"),
    QuaQua("QuaQua");
    final String s;
    Theme(String toselect) {
        s = toselect;
    }
    String getAsString() {
        return s;
    }
}
