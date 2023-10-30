package com.spotifyxp;


import com.spotifyxp.api.Player;
import com.spotifyxp.api.RestAPI;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lastfm.LastFM;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.setup.Setup;
import com.spotifyxp.stabilizer.GlobalExceptionHandler;
import com.spotifyxp.support.LinuxSupportModule;
import com.spotifyxp.support.MacOSSupportModule;
import com.spotifyxp.support.SteamDeckSupportModule;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.StartupTime;
import com.spotifyxp.webController.HttpService;

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
    static final DefThread hook = new DefThread(ContentPanel::saveCurrentState);

    public static final DefThread thread = new DefThread(new Runnable() {
        @Override
        public void run() {
            while (!past) {
                int s = Integer.parseInt(startupTime.getMMSSCoded().split(":")[1]);
                if (s > 10) {
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
        startupTime = new StartupTime();
        PublicValues.argParser.parseArguments(args);
        if(!PublicValues.nogui) {
            new SplashPanel().show();
        }else{
            new SplashPanel();
        }
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
        SplashPanel.linfo.setText("Detecting operating system...");
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            if(System.getProperty("os.name").toLowerCase().toLowerCase().contains("mac")) {
                if(!PublicValues.customSaveDir) {
                    SplashPanel.linfo.setText("Found MacOS! Applying MacOS patch...");
                    new MacOSSupportModule();
                }
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SpotifyXP");
                PublicValues.isMacOS = true;
                try {
                    Class util = Class.forName("com.apple.eawt.Application");
                    Method getApplication = util.getMethod("getApplication", new Class[0]);
                    Object application = getApplication.invoke(util);
                    Class[] params = new Class[1];
                    params[0] = Image.class;
                    Method setDockIconImage = util.getMethod("setDockIconImage", params);
                    URL url = Initiator.class.getClassLoader().getResource("spotifyxp.png");
                    Image image = Toolkit.getDefaultToolkit().getImage(url);
                    setDockIconImage.invoke(application, image);
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    //Java versions above 8 dont have com.apple.eawt.Application
                }
            }else {
                if(System.getProperty("os.name").toLowerCase().contains("steamos")) {
                    SplashPanel.linfo.setText("Found SteamOS! Applying SteamDeck patch...");
                    if(!PublicValues.customSaveDir) {
                        new LinuxSupportModule();
                    }
                    new SteamDeckSupportModule();
                }else {
                    if(!PublicValues.customSaveDir) {
                        SplashPanel.linfo.setText("Found Linux! Applying Linux patch...");
                        new LinuxSupportModule();
                    }
                }
            }
        }
        SplashPanel.linfo.setText("Checking required folders...");
        SplashPanel.linfo.setText("Initializing config...");
        PublicValues.config = new Config();
        SplashPanel.linfo.setText("Loading Extensions...");
        new Injector().autoInject();
        SplashPanel.linfo.setText("Setting up globalexceptionhandler...");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        SplashPanel.linfo.setText("Storing program arguments...");
        PublicValues.args = args;
        SplashPanel.linfo.setText("Init Language...");
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage(libLanguage.Language.getCodeFromName(PublicValues.config.getString(ConfigValues.language.name)));
        SplashPanel.linfo.setText("Parsing audio quality info...");
        try {
            PublicValues.quality = Quality.valueOf(PublicValues.config.getString(ConfigValues.audioquality.name));
        }catch (Exception exception) {
            //This should not happen but when it happens don't crash SpotifyXP
            PublicValues.quality = Quality.NORMAL;
        }
        SplashPanel.linfo.setText("Checking setup...");
        if(!PublicValues.foundSetupArgument) {
            new Setup();
            startupTime = new StartupTime();
        }
        if(!PublicValues.nogui) {
            SplashPanel.linfo.setText("Init Themes...");
            ThemeLoader loader = new ThemeLoader();
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
        try {
            Files.copy(new Resources(true).readToInputStream("SpotifyXP-Updater.jar"), Paths.get(PublicValues.appLocation + "/SpotifyXP-Updater.jar"), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            //Build without SpotifyXP-Updater
            Updater.disable = true; //Disabling updater
        }
        try {
            if(new File(PublicValues.appLocation, "LOCK").createNewFile()) {
                new File(PublicValues.appLocation, "LOCK").deleteOnExit();
            }
        }catch (Exception e) {
            ExceptionDialog.open(e);
            ConsoleLogging.Throwable(e);
            ConsoleLogging.warning("Couldn't create LOCK! SpotifyXP may be instable");
        }
        if(!PublicValues.nogui) {
            SplashPanel.linfo.setText("Checking login...");
            if (PublicValues.config.getString(ConfigValues.username.name).isEmpty()) {
                new LoginDialog().open(); //Show login dialog if no username is set
                startupTime = new StartupTime();
            }
        }
        SplashPanel.linfo.setText("Add shutdown hook...");
        Runtime.getRuntime().addShutdownHook(hook.getRawThread()); //Gets executed when SpotifyXP is closing
        Player player = null;
        if(!PublicValues.nogui) {
            SplashPanel.linfo.setText("Creating api...");
            thread.start();
            Factory.getSpotifyAPI();
            player = Factory.getPlayer();
        }
        past = true;
        if(!PublicValues.nogui) {
            SplashPanel.linfo.setText("Creating keylistener...");
            new KeyListener().start();
        }
        ContentPanel panel = null;
        if(!PublicValues.nogui) {
            SplashPanel.linfo.setText("Create advanced api key...");
            PublicValues.elevated = Factory.getPkce();
            Factory.getUnofficialSpotifyApi();
            SplashPanel.linfo.setText("Init Last.fm");
            new LastFM();
            SplashPanel.linfo.setText("Creating contentPanel...");
            if (PublicValues.isSteamDeckMode) {
                new SteamDeckSupportModule();
            }
            panel = new ContentPanel(player);
        }
        SplashPanel.linfo.setText("Starting background services...");
        new BackgroundService().start();
        if(!PublicValues.nogui) {
            Updater.UpdateInfo info = new Updater().updateAvailable();
            DefThread thread = new DefThread(() -> {
                if (info.updateAvailable) {
                    String version = info.version;
                    ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
                    new Updater().invoke();
                } else {
                    if (new Updater().isNightly()) {
                        ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.nightly"));
                        ContentPanel.feedbackupdaterdownloadbutton.setVisible(false);
                    } else {
                        ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.notavailable"));
                    }
                }
            });
            thread.start();
        }
        ConsoleLogging.info(PublicValues.language.translate("startup.info.took").replace("{}", startupTime.getMMSS()));
        if(!PublicValues.nogui) {
            SplashPanel.hide();
        }
        if(!PublicValues.nogui) {
            panel.open();
        }
        new HttpService();
        if(PublicValues.nogui) {
            new RestAPI().start();
        }
    }
}
