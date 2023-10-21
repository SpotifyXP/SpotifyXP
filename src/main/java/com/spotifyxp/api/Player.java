package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.PlayerUtils;

public class Player {
    int wait;
    com.spotifyxp.deps.xyz.gianlu.librespot.player.Player player;
    SpotifyAPI api;
    int times = 0;

    /**
     * Retries building a working librespot-player instance
     */
    public void retry() {
        player = PlayerUtils.buildPlayer();
        try {
            player.waitReady();
        } catch (InterruptedException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        } catch (NullPointerException e) {
            handleEOFBug();
            return;
        }
        ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
        player.addEventsListener(new PlayerListener(this));
        PublicValues.spotifyplayer = player;
    }

    void handleEOFBug() {
        times++;
        retry();
    }

    public Player() {

    }

    public Player(SpotifyAPI a) {
        api = a;
        player = PlayerUtils.buildPlayer();
        try {
            player.waitReady();
        } catch (InterruptedException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        } catch (NullPointerException e) {
            handleEOFBug();
            return;
        }
        ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
        player.addEventsListener(new PlayerListener(this));
        PublicValues.spotifyplayer = player;
    }

    /**
     * Returns an instance of librespot-player
     * @return    an instance of librespot-player
     */
    public com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getPlayer() {
        return player;
    }
}
