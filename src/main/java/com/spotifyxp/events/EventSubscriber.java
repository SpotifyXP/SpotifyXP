package com.spotifyxp.events;

@FunctionalInterface
public interface EventSubscriber {
    void run(Object... data);
}
