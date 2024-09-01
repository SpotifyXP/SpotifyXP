package com.spotifyxp.api;

import com.spotifyxp.PublicValues;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.utils.GraphicalMessage;

public class Player {
    com.spotifyxp.deps.xyz.gianlu.librespot.player.Player player;
    SpotifyAPI api;

    /**
     * Retries building a working librespot-player instance
     */
    public void retry() {
        player = InstanceManager.getPlayerUtils().buildPlayer();
        try {
            player.waitReady();
        } catch (InterruptedException e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
        } catch (NullPointerException e) {
            GraphicalMessage.sorryErrorExit("Failed building player");
        }
        ConsoleLogging.info(PublicValues.language.translate("debug.connection.ready"));
        player.addEventsListener(new PlayerListener(this));
        PublicValues.spotifyplayer = player;
    }

    public Player() {

    }

    /**
     * Destroys the librespot-player instance
     */
    public void destroy() {
        PublicValues.spotifyplayer.close();
        InstanceManager.setPlayer(null);
    }

    public Player(SpotifyAPI a) {
        api = a;
        player = InstanceManager.getPlayerUtils().buildPlayer();
        try {
            player.waitReady();
        } catch (InterruptedException e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
        } catch (NullPointerException e) {
            GraphicalMessage.sorryErrorExit("Failed building player");
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
