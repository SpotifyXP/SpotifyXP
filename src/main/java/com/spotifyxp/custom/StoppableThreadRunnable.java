package com.spotifyxp.custom;

@FunctionalInterface
public interface StoppableThreadRunnable {
    public abstract void run(int counter);
}
