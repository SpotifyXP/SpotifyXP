package com.spotifyxp;


import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import xyz.gianlu.librespot.core.Session;
import xyz.gianlu.librespot.player.Player;

@SuppressWarnings("CanBeFinal")
public class PublicValues {
    public static libLanguage language = null;
    public static String version = "1.0.0";
    public static String fileslocation = System.getenv("appdata") + "/SpotifyXP";
    public static String configlocation = fileslocation + "/config.toml";
    public static Player spotifyplayer = null;
    public static ConsoleLogging logger = new ConsoleLogging("SpotifyXP");
    public static SpotifyAPI.OAuthPKCE elevated = null;
    public static String configfilepath = fileslocation + "/config.properties";
    public static Config config = null;
    public static Session session;
    public static boolean debug = false;
}
