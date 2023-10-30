package com.spotifyxp.audio;

public enum Quality {
    NORMAL("Normal", "NORMAL"),
    HIGH("High", "HIGH"),
    VERY_HIGH("VeryHigh", "VERYHIGH");
    private final String s;
    private final String configValue;
    Quality(String toselect, String configValue) {
        this.s = toselect;
        this.configValue = configValue;
    }
    public String getAsString() {
        return s;
    }
    public String configValue() {
        return configValue;
    }
}
