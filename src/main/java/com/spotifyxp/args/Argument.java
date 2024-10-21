package com.spotifyxp.args;

/**
 * An interface to define an extensions main class
 */
public interface Argument {
    Runnable runArgument(String commands);

    String getName();

    String getDescription();

    boolean hasParameter();
}
