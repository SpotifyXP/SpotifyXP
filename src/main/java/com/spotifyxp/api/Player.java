package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.PlayerUtils;

import java.io.EOFException;

public class Player {
    int wait;
    com.spotifyxp.deps.xyz.gianlu.librespot.player.Player player;
    SpotifyAPI api;
    int times = 0;

    public void retry() {
        try {
            player = PlayerUtils.buildPlayer();
        }catch (EOFException e) {
            if(times > 5) {
                GraphicalMessage.sorryErrorExit("Couldn't build player");
                return;
            }
            handleEOFBug();
            return;
        }
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
        try {
            player = PlayerUtils.buildPlayer();
        }catch (EOFException e) {
            handleEOFBug();
            return;
        }
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

    public com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getPlayer() {
        return player;
    }
}
