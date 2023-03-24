package com.spotifyxp.audio;

public enum Quality {
    NORMAL("Normal"),
    HIGH("High"),
    VERY_HIGH("VeryHigh");
    final String s;
    Quality(String toselect) {
        s = toselect;
    }
    String getAsString() {
        return s;
    }
}
