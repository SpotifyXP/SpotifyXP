package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Token;

public class Test {
    public static void main(String[] args) {
        PublicValues.config = new Config();
        PublicValues.spotifyplayer = PlayerUtils.buildPlayer();
        UnofficialSpotifyAPI spotifyAPI = new UnofficialSpotifyAPI(Token.getDefaultToken());
        spotifyAPI.getHomeTab();
    }
}
