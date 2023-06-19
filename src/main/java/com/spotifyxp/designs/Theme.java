package com.spotifyxp.designs;

public enum Theme {
    DARK("Dark", false),
    LIGHT("Light", true),
    WINDOWS("Windows", true),
    MacOSDark("MacOSDark", false),
    MacOSLight("MacOSLight", true),
    QuaQua("QuaQua", true),
    LEGACY("Legacy", true),
    UGLY("Ugly", true),
    DARKGREEN("DarkGreen", false);
    final String s;
    final boolean isLight;
    Theme(String toselect, boolean light) {
        s = toselect;
        isLight = light;
    }
    public String getAsString() {
        return s;
    }
    public boolean isDark() {
        return !isLight;
    }
}
