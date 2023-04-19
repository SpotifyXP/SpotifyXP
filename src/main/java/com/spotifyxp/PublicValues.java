package com.spotifyxp;


import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.dialogs.LyricsDialog;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;

@SuppressWarnings("CanBeFinal")
public class PublicValues {
    public static libLanguage language = null;
    public static String version = "1.8";
    public static String fileslocation = System.getenv("appdata") + "/SpotifyXP";
    public static String configlocation = fileslocation + "/config.toml";
    public static Player spotifyplayer = null;
    public static ConsoleLogging logger = new ConsoleLogging("SpotifyXP");
    public static SpotifyAPI.OAuthPKCE elevated = null;
    public static String configfilepath = fileslocation + "/config.properties";
    public static Config config = null;
    public static Session session;
    public static boolean debug = false;
    public static GraphiteLookAndFeel windowTheme = new GraphiteLookAndFeel();
    public static Quality quality = null;
    public static String[] args = null;
    public static String appLocation = System.getProperty("user.home") + "/SpotifyXP";
    //public static Theme theme = null;
    public static Theme theme = null;
    public static boolean foundSetupArgument = false;
    public static LyricsDialog lyricsDialog = null;
}
