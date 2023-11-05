package com.spotifyxp.console.commands;

public interface Command {
    Runnable runArgument(String parameter1);
    String getName();
    String getDescription();
    boolean hasParameter();
}
