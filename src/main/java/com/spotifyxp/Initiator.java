package com.spotifyxp;

import com.spotifyxp.api.Player;
import com.spotifyxp.api.RestAPI;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lastfm.LastFM;
import com.spotifyxp.lib.libDetect;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.Feedback;
import com.spotifyxp.panels.PlayerArea;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.setup.Setup;
import com.spotifyxp.stabilizer.GlobalExceptionHandler;
import com.spotifyxp.support.LinuxSupportModule;
import com.spotifyxp.support.MacOSSupportModule;
import com.spotifyxp.support.SteamDeckSupportModule;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.ApplicationUtils;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.StartupTime;
import com.spotifyxp.web.WebInterface;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@SuppressWarnings({"all", "RedundantArrayCreation"})
public class Initiator {
    public static StartupTime startupTime;
    static final DefThread hook = new DefThread(PlayerArea::saveCurrentState);

    static int destroyCounter = 1;

    public static final DefThread thread = new DefThread(new Runnable() {
        @Override
        public void run() {
            while (!past) {
                int s = Integer.parseInt(startupTime.getMMSSCoded().split(":")[1]);
                if (s > 10) {
                    if(!(destroyCounter > 2)) {
                        ConsoleLogging.warning("Init of player failed! Retrying... (" + destroyCounter + ")");
                        InstanceManager.getPlayer().destroy();
                        Thread playerBuildThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                InstanceManager.getPlayer();
                            }
                        });
                        playerBuildThread.start();
                        destroyCounter++;
                        return;
                    }
                    if(GraphicalMessage.stuck()) {
                        System.exit(0);
                    }else{
                        past = true;
                        break;
                    }
                }
            }
        }
    });



    public static boolean past = false;
    @SuppressWarnings("rawtypes")
    public static void main(String[] args) {
        startupTime = new StartupTime(); //Saving the time SpotifyXP was started
        PublicValues.argParser.parseArguments(args); //Parsing the arguments
        initSplashPanel(); //Initializing the splash panel
        System.setProperty("http.agent", ApplicationUtils.getUserAgent()); //Setting the user agent string that SpotifyXP uses
        checkDebug(); //Checking if debug is enabled
        detectOS(); //Detecting the operating system
        checkSetup();
        initEvents(); //Initializing the event support
        initConfig(); //Initializing the configuratio
        loadExtensions(); //Loading extensions if there are any
        initGEH(); //Initializing the global exception handler
        storeArguments(args); //Storing the program arguments in PublicValues.class
        initLanguageSupport(); //Initializing the language support
        parseAudioQuality(); //Parsing the audio quality

        if(PublicValues.nogui) {
            initNoGUI();
            return;
        }

        initThemes(); //Initializing the theming support
        checkingUpdater(); //Checking if the updater jar exists inside resources
        creatingLock(); //Creating the 'LOCK' file
        checkLogin(); //Checking if user has already entered his credentials
        addShutdownHook(); //Adding the shudtown hook
        initAPI(); //Initializing all the apis used
        createKeyListener(); //Starting the key listener (For Play/Pause)
        initTrayIcon(); //Creating the tray icon
        initGUI(); //Initializing the GUI
        checkingUpdate(); //Checking for available updates
        ConsoleLogging.info(PublicValues.language.translate("startup.info.took").replace("{}", startupTime.getMMSS()));
        SplashPanel.hide(); //Hiding the splash panel
        new WebInterface(); //Starting the webinterface
    }

    static void initNoGUI() {
        creatingLock(); //Creating the 'LOCK' file
        checkLogin(); //Checking if user has already entered his credentials
        addShutdownHook(); //Adding the shudtown hook
        initAPI(); //Initializing all the apis used
        ConsoleLogging.info(PublicValues.language.translate("startup.info.took").replace("{}", startupTime.getMMSS()));
        new WebInterface(); //Starting the webinterface
        new RestAPI().start(); //Starting the restAPI
    }

    static void initSplashPanel() {
        if(!PublicValues.nogui) {
            new SplashPanel().show();
        }else{
            new SplashPanel();
        }
    }

    static void checkDebug() {
        if(PublicValues.debug) {
            PublicValues.logger.setColored(!System.getProperty("os.name").toLowerCase().contains("win"));
            ConsoleLoggingModules modules = new ConsoleLoggingModules();
            modules.setColored(!System.getProperty("os.name").toLowerCase().contains("win"));
        }else{
            System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
                @Override public void write(int b) {}
            }) {
                @Override public void flush() {}
                @Override public void close() {}
                @Override public void write(int b) {}
                @Override public void write(byte[] b) {}
                @Override public void write(byte[] buf, int off, int len) {}
                @Override public void print(boolean b) {}
                @Override public void print(char c) {}
                @Override public void print(int i) {}
                @Override public void print(long l) {}
                @Override public void print(float f) {}
                @Override public void print(double d) {}
                @Override public void print(char[] s) {}
                @Override public void print(String s) {}
                @Override public void print(Object obj) {}
                @Override public void println() {}
                @Override public void println(boolean x) {}
                @Override public void println(char x) {}
                @Override public void println(int x) {}
                @Override public void println(long x) {}
                @Override public void println(float x) {}
                @Override public void println(double x) {}
                @Override public void println(char[] x) {}
                @Override public void println(String x) {}
                @Override public void println(Object x) {}
                @Override public java.io.PrintStream printf(String format, Object... args) { return this; }
                @Override public java.io.PrintStream printf(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq, int start, int end) { return this; }
                @Override public java.io.PrintStream append(char c) { return this; }
            });
            System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                @Override public void write(int b) {}
            }) {
                @Override public void flush() {}
                @Override public void close() {}
                @Override public void write(int b) {}
                @Override public void write(byte[] b) {}
                @Override public void write(byte[] buf, int off, int len) {}
                @Override public void print(boolean b) {}
                @Override public void print(char c) {}
                @Override public void print(int i) {}
                @Override public void print(long l) {}
                @Override public void print(float f) {}
                @Override public void print(double d) {}
                @Override public void print(char[] s) {}
                @Override public void print(String s) {}
                @Override public void print(Object obj) {}
                @Override public void println() {}
                @Override public void println(boolean x) {}
                @Override public void println(char x) {}
                @Override public void println(int x) {}
                @Override public void println(long x) {}
                @Override public void println(float x) {}
                @Override public void println(double x) {}
                @Override public void println(char[] x) {}
                @Override public void println(String x) {}
                @Override public void println(Object x) {}
                @Override public java.io.PrintStream printf(String format, Object... args) { return this; }
                @Override public java.io.PrintStream printf(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(String format, Object... args) { return this; }
                @Override public java.io.PrintStream format(java.util.Locale l, String format, Object... args) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq) { return this; }
                @Override public java.io.PrintStream append(CharSequence csq, int start, int end) { return this; }
                @Override public java.io.PrintStream append(char c) { return this; }
            });
        }
    }

    static void detectOS() {
        SplashPanel.linfo.setText("Detecting operating system...");
        PublicValues.osType = libDetect.getDetectedOS();
        switch(PublicValues.osType) {
            case Linux:
                if(!PublicValues.customSaveDir) {
                    SplashPanel.linfo.setText("Found Linux! Applying Linux patch...");
                    new LinuxSupportModule();
                }
                break;
            case MacOS:
                if(!PublicValues.customSaveDir) {
                    SplashPanel.linfo.setText("Found MacOS! Applying MacOS patch...");
                    new MacOSSupportModule();
                }
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SpotifyXP");
                try {
                    Class<?> util = Class.forName("com.apple.eawt.Application");
                    Method getApplication = util.getMethod("getApplication", new Class[0]);
                    Object application = getApplication.invoke(util);
                    Class[] params = new Class[1];
                    params[0] = Image.class;
                    Method setDockIconImage = util.getMethod("setDockIconImage", params);
                    URL url = Initiator.class.getClassLoader().getResource("spotifyxp.png");
                    Image image = Toolkit.getDefaultToolkit().getImage(url);
                    setDockIconImage.invoke(application, image);
                }catch (Exception ignored) {
                    try {
                        Class<?> util = Class.forName("java.awt.Taskbar");
                        Method getApplication = util.getMethod("getTaskbar", new Class[0]);
                        Object application = getApplication.invoke(util);
                        Class[] params = new Class[1];
                        params[0] = Image.class;
                        Method setDockIconImage = util.getMethod("setIconImage", params);
                        URL url = Initiator.class.getClassLoader().getResource("spotifyxp.png");
                        Image image = Toolkit.getDefaultToolkit().getImage(url);
                        setDockIconImage.invoke(application, image);
                    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    }
                }
                break;
            case Steamos:
                SplashPanel.linfo.setText("Found SteamOS! Applying SteamDeck patch...");
                if(!PublicValues.customSaveDir) {
                    new LinuxSupportModule();
                }
                new SteamDeckSupportModule();
                break;
        }
    }

    static void initEvents() {
        for(SpotifyXPEvents s : SpotifyXPEvents.values()) {
            Events.register(s.getName(), true);
        }
    }

    static void initConfig() {
        SplashPanel.linfo.setText("Initializing config...");
        PublicValues.config = new Config();
        PublicValues.config.checkConfig();
    }

    static void loadExtensions() {
        SplashPanel.linfo.setText("Loading Extensions...");
        new Injector().autoInject();
    }

    static void initGEH() {
        SplashPanel.linfo.setText("Setting up globalexceptionhandler...");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
    }

    static void storeArguments(String[] args) {
        SplashPanel.linfo.setText("Storing program arguments...");
        PublicValues.args = args;
    }

    static void initLanguageSupport() {
        SplashPanel.linfo.setText("Init Language...");
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage(libLanguage.Language.getCodeFromName(PublicValues.config.getString(ConfigValues.language.name)));
    }

    static void parseAudioQuality() {
        SplashPanel.linfo.setText("Parsing audio quality info...");
        try {
            PublicValues.quality = Quality.valueOf(PublicValues.config.getString(ConfigValues.audioquality.name));
        }catch (Exception exception) {
            //This should not happen but when it happens don't crash SpotifyXP
            PublicValues.quality = Quality.NORMAL;
            ConsoleLogging.warning("Can't find the right audio quality! Defaulting to 'NORMAL'");
        }
    }

    static void checkSetup() {
        SplashPanel.linfo.setText("Checking setup...");
        if(!PublicValues.foundSetupArgument) {
            new Setup();
            startupTime = new StartupTime();
        }
    }

    static void initThemes() {
        SplashPanel.linfo.setText("Init Themes...");
        ThemeLoader loader = PublicValues.themeLoader;
        try {
            loader.loadTheme(PublicValues.config.getString(ConfigValues.theme.name));
        } catch (ThemeLoader.UnknownThemeException e) {
            ConsoleLogging.warning("Unknown Theme: '" + PublicValues.config.getString(ConfigValues.theme.name) + "'! Trying to load theme differently");
            try {
                loader.tryLoadTheme(PublicValues.config.getString(ConfigValues.theme.name));
            } catch (Exception e2) {
                ConsoleLogging.warning("Failed loading theme! SpotifyXP is now ugly");
            }
        }
    }

    static void checkingUpdater() {
        try {
            Files.copy(new Resources(true).readToInputStream("SpotifyXP-Updater.jar"), Paths.get(PublicValues.appLocation + "/SpotifyXP-Updater.jar"), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            //Build without SpotifyXP-Updater
            Updater.disable = true; //Disabling updater
        }
    }

    static void creatingLock() {
        try {
            if(new File(PublicValues.appLocation, "LOCK").createNewFile()) {
                new File(PublicValues.appLocation, "LOCK").deleteOnExit();
            }
        }catch (Exception e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
            ConsoleLogging.warning("Couldn't create LOCK! SpotifyXP may be instable");
        }
    }

    static void checkLogin() {
        SplashPanel.linfo.setText("Checking login...");
        if (PublicValues.config.getString(ConfigValues.username.name).isEmpty()) {
            new LoginDialog().open(); //Show login dialog if no username is set
            startupTime = new StartupTime();
        }
    }

    static void addShutdownHook() {
        SplashPanel.linfo.setText("Add shutdown hook...");
        Runtime.getRuntime().addShutdownHook(hook.getRawThread());
    }

    static void createKeyListener() {
        SplashPanel.linfo.setText("Creating keylistener...");
        new KeyListener().start();
    }

    static void initAPI() {
        SplashPanel.linfo.setText("Creating api...");
        Player player = null;
        thread.start();
        InstanceManager.getSpotifyAPI();
        player = InstanceManager.getPlayer();
        past = true;
        SplashPanel.linfo.setText("Create advanced api key...");
        InstanceManager.getUnofficialSpotifyApi();
        SplashPanel.linfo.setText("Init Last.fm");
        new LastFM();
    }

    static void initGUI() {
        SplashPanel.linfo.setText("Creating contentPanel...");
        if (PublicValues.osType == libDetect.OSType.Steamos) {
            new SteamDeckSupportModule();
        }
        ContentPanel panel = new ContentPanel();
        panel.open();
    }

    static void initTrayIcon() {
        SplashPanel.linfo.setText("Creating the tray icon...");
        new BackgroundService().start();
    }

    static void checkingUpdate() {
        Updater.UpdateInfo info = new Updater().updateAvailable();
        DefThread thread = new DefThread(() -> {
            if (info.updateAvailable) {
                String version = info.version;
                Feedback.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
                new Updater().invoke();
            } else {
                if (new Updater().isNightly()) {
                    Feedback.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.nightly"));
                    Feedback.feedbackupdaterdownloadbutton.setVisible(false);
                } else {
                    Feedback.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.notavailable"));
                }
            }
        });
        thread.start();
    }
}
