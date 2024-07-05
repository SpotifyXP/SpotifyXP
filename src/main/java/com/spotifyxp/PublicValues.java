package com.spotifyxp;


import com.neovisionaries.i18n.CountryCode;
import com.spotifyxp.args.ArgParser;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.xyz.gianlu.librespot.core.Session;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.PlayerDefine;
import com.spotifyxp.dialogs.CancelDialog;
import com.spotifyxp.dialogs.LyricsDialog;
import com.spotifyxp.history.PlaybackHistory;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lib.libDetect;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.threading.ThreadManager;
import com.spotifyxp.utils.ArchitectureDetection;
import com.spotifyxp.utils.Utils;
import com.spotifyxp.visuals.AudioVisualizer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("CanBeFinal")
public class PublicValues {
    public static int applicationHeight = 590;
    public static int applicationWidth = 784;
    public static Dimension getApplicationDimensions() {
        return new Dimension(applicationWidth, applicationHeight);
    }
    public static ArchitectureDetection.Architecture architecture = ArchitectureDetection.Architecture.x86;
    public static CancelDialog facebookcanceldialog;
    public static boolean playingFromLibrary = false;
    public static libLanguage language = null;
    public static String fileslocation = System.getenv("appdata") + "/SpotifyXP";
    public static PlayerDefine spotifyplayer = null;
    public static ConsoleLogging logger = new ConsoleLogging();
    public static String configfilepath = fileslocation + "/config.json";
    public static Config config = null;
    public static Session session;
    public static boolean debug = false;
    public static Quality quality = null;
    public static String[] args = null;
    public static String appLocation = System.getProperty("user.home") + "/SpotifyXP";
    public static String deviceName = "SpotifyXP";
    public static Theme theme = null;
    public static libDetect.OSType osType;
    public static boolean foundSetupArgument = false;
    public static Color globalFontColor = Color.black;
    public static LyricsDialog lyricsDialog = null;
    public static Injector injector = new Injector();
    public static ContentPanel contentPanel;
    public static ArgParser argParser = new ArgParser();
    public static boolean customSaveDir = false;
    public static AudioVisualizer visualizer = new AudioVisualizer();
    public static ArrayList<ContextMenu.GlobalContextMenuItem> globalContextMenuItems = new ArrayList<>();
    public static String tempPath = System.getenv("temp");
    public static ArrayList<ContextMenu> contextMenus = new ArrayList<>();
    public static boolean blockArtistPanelBackButton = false;
    public static Color borderColor = Color.black;
    public static boolean autoLoadHotList = false;
    public static PlaybackHistory history;
    public static ThreadManager threadManager = new ThreadManager();
    public static boolean blockLoading = false;
    public static boolean devMode = false;
    public static JMenuBar menuBar;
    public static int screenNumber = Utils.getDefaultScreenNumber();
    public static boolean shuffle = false;
    public static ThemeLoader themeLoader;
    public static boolean wasOffline;
    public static CountryCode countryCode;
    public static boolean enableMediaControl = true;
    public static boolean disableChunkDebug = false;
    //Devstuff
    public static boolean locationFinderActive = false;
    //----
}
