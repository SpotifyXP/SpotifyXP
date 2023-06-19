package com.spotifyxp.custom;

@FunctionalInterface
public interface StoppableThreadRunnable {
    void run(int counter);
}
