package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.com.spotify.extendedmetadata.ExtendedMetadata;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.updater.UpdaterDialog;
import com.spotifyxp.utils.ConnectionUtils;
import com.spotifyxp.utils.PlayerUtils;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static void main(String[] args ) {
        PublicValues.config = new Config();
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage("en");
        SpotifyAPI.Player player = new SpotifyAPI.Player(new SpotifyAPI());
        PublicValues.elevated = new SpotifyAPI.OAuthPKCE();
        try {
            UnofficialSpotifyAPI api = new UnofficialSpotifyAPI(PublicValues.session.tokens().getToken("ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private".split(" ")).accessToken);
            api.getArtist("spotify:artist:7vk5e3vY1uw9plTHJAMwjN");
        } catch (IOException | MercuryClient.MercuryException e) {
            e.printStackTrace();
        }

    }

}
