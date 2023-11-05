package com.spotifyxp.console.commands;

import com.spotifyxp.console.Console;

public class HelpCommand implements Command{
    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                Console.printHelp();
            }
        };
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Prints this message";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
