package com.spotifyxp.designs;

public enum Theme {
    DARK("Dark"),
    LIGHT("Light"),
    WINDOWS("Windows"),
    MacOSDark("MacOSDark"),
    MacOSLight("MacOSLight"),
    QuaQua("QuaQua"),
    LEGACY("Legacy");
    final String s;
    Theme(String toselect) {
        s = toselect;
    }
    public String getAsString() {
        return s;
    }
}
