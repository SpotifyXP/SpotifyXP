package com.spotifyxp.args;

import com.spotifyxp.PublicValues;

public class NoMediaControl implements Argument {

    @Override
    public Runnable runArgument(String commands) {
        return () -> PublicValues.enableMediaControl = false;
    }

    @Override
    public String getName() {
        return "disablemediacontrol";
    }

    @Override
    public String getDescription() {
        return "Disbles media control per media keys";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
