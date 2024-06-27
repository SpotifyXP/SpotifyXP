package com.spotifyxp.dummy;

import com.spotifyxp.api.UnofficialSpotifyAPI;

public class DummyUnofficialSpotifyAPI extends UnofficialSpotifyAPI {
    public DummyUnofficialSpotifyAPI() {
        super("");
    }

    @Override
    public HomeTab getHomeTab() {
        return new HomeTab();
    }
}
