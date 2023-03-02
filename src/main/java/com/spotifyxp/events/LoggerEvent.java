package com.spotifyxp.events;

public interface LoggerEvent {
    void log(String message);
    void err(String message);
    void info(String message);
    void crit(String message);
}
