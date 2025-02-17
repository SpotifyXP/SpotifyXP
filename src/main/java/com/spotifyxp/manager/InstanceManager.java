package com.spotifyxp.manager;

import com.spotifyxp.api.OAuthPKCE;
import com.spotifyxp.api.Player;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyHttpManager;
import com.spotifyxp.utils.PlayerUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;

/**
 * This class is a manager
 *
 * <br> Get Example: getUnofficialSpotifyApi()
 * <br> Set Example: setUnofficialSpotifyApi( [instance of UnofficialSpotifyAPI] )
 */
public class InstanceManager {
    static SpotifyAPI api;
    static SpotifyApi sapi;
    static OAuthPKCE pkce;
    static Player player;
    static UnofficialSpotifyAPI unofficialSpotifyAPI;
    static PlayerUtils playerUtils;

    public static Player getPlayer() {
        if (player == null) {
            player = new Player(api);
        }
        return player;
    }

    public static com.spotifyxp.deps.xyz.gianlu.librespot.player.Player getSpotifyPlayer() {
        if (player == null) {
            player = new Player(api);
        }
        return player.getPlayer();
    }

    public static void setPlayer(Player p) {
        player = p;
    }

    public static OAuthPKCE getPkce() {
        if (pkce == null) {
            pkce = new OAuthPKCE();
        }
        return pkce;
    }

    public static UnofficialSpotifyAPI getUnofficialSpotifyApi() {
        if (unofficialSpotifyAPI == null) {
            unofficialSpotifyAPI = new UnofficialSpotifyAPI();
        }
        return unofficialSpotifyAPI;
    }

    public static void setUnofficialSpotifyAPI(UnofficialSpotifyAPI api) {
        unofficialSpotifyAPI = api;
    }

    public static void setPkce(OAuthPKCE pk) {
        pkce = pk;
    }

    public static SpotifyAPI getSpotifyAPI() {
        if (api == null) {
            api = new SpotifyAPI();
        }
        return api;
    }

    public static PlayerUtils getPlayerUtils() {
        if (playerUtils == null) {
            playerUtils = new PlayerUtils();
        }
        return playerUtils;
    }

    public static void setPlayerUtils(PlayerUtils utils) {
        playerUtils = utils;
    }

    public static void setSpotifyAPI(SpotifyAPI a) {
        api = a;
    }

    public static void setSpotifyApi(SpotifyApi a) {
        sapi = a;
    }

    public static SpotifyApi getSpotifyApi() {
        if (sapi == null) {
            sapi = SpotifyApi.builder().setAccessToken(pkce.getToken()).build();
        }
        return sapi;
    }

    public static void destroy() {
        api = null;
        sapi = null;
        pkce = null;
        player = null;
        unofficialSpotifyAPI = null;
        playerUtils = null;
    }
}
