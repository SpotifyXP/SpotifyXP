package com.spotifyxp.args;

import com.spotifyxp.api.RestAPI;
import com.spotifyxp.injector.InjectingPoints;

public class RESTApi implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return new Runnable() {
            @Override
            public void run() {
                InjectingPoints.registerOnFrameReady(new Runnable() {
                    @Override
                    public void run() {
                        new RestAPI().start();
                    }
                });
            }
        };
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
