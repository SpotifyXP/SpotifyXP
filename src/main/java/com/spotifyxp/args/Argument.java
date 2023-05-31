package com.spotifyxp.args;

public interface Argument {
    Runnable runArgument(String parameter1);
    String getName();
    String getDescription();
    boolean hasParameter();
}
