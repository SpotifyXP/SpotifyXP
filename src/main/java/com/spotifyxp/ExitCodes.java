package com.spotifyxp;

public enum ExitCodes {
    USER_DECISION(1),
    PORT_BLOCKING_DETECTED(5),
    PROXY_NOT_RECHEABLE(9);
    @SuppressWarnings({"NonFinalFieldInEnum", "CanBeFinal"})
    int selected;
    ExitCodes(int code) {
        selected = code;
    }
    public int getCode() {
        return selected;
    }
}
