package com.spotifyxp.audio;

/**
 * Holds all available audio qualities<br><br>
 * - Normal<br>
 * - High<br>
 * - Very high
 */
public enum Quality {
    NORMAL("Normal", "NORMAL"),
    HIGH("High", "HIGH"),
    VERY_HIGH("VeryHigh", "VERYHIGH");
    private final String toselect;
    private final String configValue;

    Quality(String toselect, String configValue) {
        this.toselect = toselect;
        this.configValue = configValue;
    }

    /**
     * Returns the audio quality as a readable string
     *
     * @return String
     */
    public String getAsString() {
        return toselect;
    }

    /**
     * Returns the audio quality as a config value
     *
     * @return String
     */
    public String configValue() {
        return configValue;
    }
}
