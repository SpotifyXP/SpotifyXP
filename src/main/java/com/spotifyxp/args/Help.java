package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class Help implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                PublicValues.argParser.printHelp();
                System.exit(0);
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
