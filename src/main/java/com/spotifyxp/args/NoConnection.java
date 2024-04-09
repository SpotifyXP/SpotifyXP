package com.spotifyxp.args;

import com.spotifyxp.dummy.*;
import com.spotifyxp.manager.InstanceManager;

public class NoConnection implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> {
            InstanceManager.setPkce(new DummyOAuthPKCE());
            InstanceManager.setPlayer(new DummyPlayer());
            InstanceManager.setSpotifyAPI(new DummySpotifyAPI());
            InstanceManager.setSpotifyApi(new DummySpapi());
            InstanceManager.setUnofficialSpotifyAPI(new DummyUnofficialSpotifyAPI());
        };
    }

    @Override
    public String getName() {
        return "no-connection";
    }

    @Override
    public String getDescription() {
        return "Disabling network traffic to Spotify";
    }

    @Override
    public boolean hasParameter() {
        return false;
    }
}
