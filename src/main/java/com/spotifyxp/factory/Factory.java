package com.spotifyxp.factory;

import com.spotifyxp.api.OAuthPKCE;
import com.spotifyxp.api.Player;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.deps.se.michaelthelin.spotify.SpotifyApi;

public class Factory {
    static SpotifyAPI api;
    static SpotifyApi sapi;
    static OAuthPKCE pkce;
    static Player player;
    static UnofficialSpotifyAPI unofficialSpotifyAPI;

    public static Player getPlayer() {
        if(player == null) {
            player = new Player(api);
        }
        return player;
    }

    public static void setPlayer(Player p) {
        player = p;
    }

    public static OAuthPKCE getPkce() {
        if(pkce == null) {
            pkce = new OAuthPKCE(true);
        }
        return pkce;
    }

    public static UnofficialSpotifyAPI getUnofficialSpotifyApi() {
        if(unofficialSpotifyAPI == null) {
            unofficialSpotifyAPI = new UnofficialSpotifyAPI(Factory.getPkce().getToken());
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
        if(api == null) {
            api = new SpotifyAPI();
        }
        return api;
    }

    public static void setSpotifyAPI(SpotifyAPI a) {
        api = a;
    }

    public static void setSpotifyApi(SpotifyApi a) {
        sapi = a;
    }

    public static SpotifyApi getSpotifyApi() {
        if(sapi == null) {
            sapi = SpotifyApi.builder().setAccessToken(pkce.getToken()).build();
        }
        return sapi;
    }
}
