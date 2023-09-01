package com.spotifyxp.dummy;

import com.spotifyxp.api.Player;
import com.spotifyxp.api.SpotifyAPI;

public class DummyPlayer extends Player {
    public DummyPlayer() {

    }

    @Override
    public com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getPlayer() {
        return new DummyGianluPlayer();
    }
}
