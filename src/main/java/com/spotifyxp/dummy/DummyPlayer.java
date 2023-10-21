package com.spotifyxp.dummy;

import com.spotifyxp.api.Player;

public class DummyPlayer extends Player {
    public DummyPlayer() {

    }

    @Override
    public com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getPlayer() {
        return new DummyGianluPlayer();
    }
}
