package com.spotifyxp.dummy;

import com.spotifyxp.events.LoggerEvent;

public class NullLogger implements LoggerEvent {
    @Override
    public void log(String message) {

    }

    @Override
    public void err(String message) {

    }

    @Override
    public void info(String message) {

    }

    @Override
    public void crit(String message) {

    }
}
