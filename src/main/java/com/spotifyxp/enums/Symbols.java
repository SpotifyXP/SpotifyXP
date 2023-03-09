package com.spotifyxp.enums;

public enum Symbols {
    NONE("null"),
    ERROR("/error.png"),
    CRITICAL("/critical.png"),
    INFO("/info.png"),
    WARNING("/warning.png");
    @SuppressWarnings({"NonFinalFieldInEnum", "CanBeFinal"})
    String selected;
    Symbols(String path) {
        selected = path;
    }
    public String getSelected() {
        return selected;
    }
}
