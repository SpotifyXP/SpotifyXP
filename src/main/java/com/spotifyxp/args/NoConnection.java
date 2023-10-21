package com.spotifyxp.args;

import com.spotifyxp.dummy.*;
import com.spotifyxp.factory.Factory;

public class NoConnection implements Argument {
    @Override
    public Runnable runArgument(String parameter1) {
        return () -> {
            Factory.setPkce(new DummyOAuthPKCE());
            Factory.setPlayer(new DummyPlayer());
            Factory.setSpotifyAPI(new DummySpotifyAPI());
            Factory.setSpotifyApi(new DummySpapi());
            Factory.setUnofficialSpotifyAPI(new DummyUnofficialSpotifyAPI());
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
