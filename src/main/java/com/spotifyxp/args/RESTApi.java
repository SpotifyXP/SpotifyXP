package com.spotifyxp.args;

import com.spotifyxp.api.RestAPI;
import com.spotifyxp.events.Events;

public class RESTApi implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> Events.registerOnFrameReadyEvent(() -> new RestAPI().start());
    }

    @Override
    public String getName() {
        return "restapi";
    }

    @Override
    public String getDescription() {
        return "Starts the SpotifyXP RestAPI";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
