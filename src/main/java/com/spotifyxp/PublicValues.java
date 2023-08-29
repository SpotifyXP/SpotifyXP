package com.spotifyxp;


import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.args.ArgParser;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.dialogs.LyricsDialog;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.video.CanvasPlayer;
import com.spotifyxp.visuals.AudioVisualizer;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class PublicValues {
    public static boolean playingFromLibrary = false;
    public static String apikey = "";
    public static libLanguage language = null;
    public static String fileslocation = System.getenv("appdata") + "/SpotifyXP";
    public static String startmenupath = System.getenv("appdata") + "/Microsoft/Windows/Start Menu/Programs";
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
    public static String deviceName = "SpotifyXP";
    public static boolean isMacOS = false;
    public static boolean isLinux = false;
    public static Theme theme = null;
    public static boolean foundSetupArgument = false;
    public static Color globalFontColor = Color.black;
    public static LyricsDialog lyricsDialog = null;
    public static Injector injector = new Injector();
    public static ContentPanel contentPanel;
    public static ArgParser argParser = new ArgParser();
    public static  boolean customSaveDir = false;
    public static AudioVisualizer visualizer = new AudioVisualizer();
    public static ArrayList<ContextMenu.GlobalContextMenuItem> globalContextMenuItems = new ArrayList<>();
    public static boolean isSteamDeckMode = false;
    public static String tempPath = System.getenv("temp");
    public static CanvasPlayer canvasPlayer;
}
