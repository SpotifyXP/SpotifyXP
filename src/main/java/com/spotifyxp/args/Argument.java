package com.spotifyxp.args;

public interface Argument {
    Runnable runArgument(String commands);
    String getName();
    String getDescription();
    boolean hasParameter();
}
