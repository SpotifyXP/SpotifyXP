package com.spotifyxp.events;

@SuppressWarnings("EmptyMethod")
public interface LoggerEvent {
    void log(String message);
    void err(String message);
    void info(String message);
    void crit(String message);
}
