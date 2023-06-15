package com.spotifyxp;


import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.args.ArgParser;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.beamngintegration.HttpService;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.setup.Setup;
import com.spotifyxp.stabilizer.GlobalExceptionHandler;
import com.spotifyxp.support.LinuxSupportModule;
import com.spotifyxp.support.SteamDeckSupportModule;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.theming.themes.Ugly;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.updater.UpdaterDialog;
import com.spotifyxp.utils.DoubleArrayList;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.StartupTime;
import javax.swing.*;
import java.io.File;

@SuppressWarnings("Convert2Lambda")
public class Initiator {
    static SpotifyAPI api = null;
    public static StartupTime startupTime;
    static DefThread hook = new DefThread(new Runnable() {
        @Override
        public void run() {
            ContentPanel.saveCurrentState();
        }
    });
    public static DefThread thread = new DefThread(new Runnable() {
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
    public static void main(String[] args) {
        new SplashPanel().show();
        SplashPanel.linfo.setText("Storing startup millis...");
        startupTime = new StartupTime();
        SplashPanel.linfo.setText("Setting up logging...");
        PublicValues.logger.setColored(false);
        PublicValues.logger.setShowTime(false);
        SplashPanel.linfo.setText("Loading Extensions...");
        new Injector().autoInject();
        SplashPanel.linfo.setText("Setting up multilanguage support...");
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage("en");
        SplashPanel.linfo.setText("Setting up globalexceptionhandler...");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        SplashPanel.linfo.setText("Parsing arguments...");
        PublicValues.argParser.parseArguments(args);
        SplashPanel.linfo.setText("Detecting operating system...");
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            //Is not Windows
            SplashPanel.linfo.setText("Found Linux! Applying Linux patch...");
            new LinuxSupportModule();
            args = new String[]{"--setup-complete"};
        }
        SplashPanel.linfo.setText("Storing program arguments...");
        PublicValues.args = args;
        SplashPanel.linfo.setText("Checking required folders...");
        if(!new File(PublicValues.fileslocation).exists()) {
            if(!new File(PublicValues.fileslocation).mkdir()) {
                ConsoleLogging.changeName("SpotifyAPI");
                ConsoleLogging.error(PublicValues.language.translate("error.configuration.failedcreate"));
            }
        }
        SplashPanel.linfo.setText("Detecting debugging...");
        if(new File("pom.xml").exists()) {
            PublicValues.debug = true;
            ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
            if(!System.getProperty("os.name").toLowerCase().contains("xp")) {
                modules.setColored(true);
            }else{
                modules.setColored(false);
            }
            modules.setShowTime(false);
            if(!System.getProperty("os.name").toLowerCase().contains("xp")) {
                PublicValues.logger.setColored(true);
            }else{
                PublicValues.logger.setColored(false);
            }
            PublicValues.foundSetupArgument = true;
        }
        SplashPanel.linfo.setText("Initializing config...");
        PublicValues.config = new Config();
        SplashPanel.linfo.setText("Init Themes...");
        ThemeLoader loader = new ThemeLoader();
        try {
            loader.loadTheme(PublicValues.config.get(ConfigValues.theme.name));
        } catch (ThemeLoader.UnknownThemeException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
            try {
                loader.loadTheme("Ugly");
            }catch (Exception e2) {
                GraphicalMessage.bug("Can't load any theme");
            }
        }
        SplashPanel.linfo.setText("Parsing audio quality info...");
        try {
            PublicValues.quality = Quality.valueOf(PublicValues.config.get(ConfigValues.audioquality.name));
        }catch (IllegalArgumentException exception) {
            //This should not happen but when it happens don't crash SpotifyXP
            PublicValues.quality = Quality.NORMAL;
        }
        SplashPanel.linfo.setText("Checking setup...");
        if(!PublicValues.foundSetupArgument) {
            new Setup(); //Start setup because the argument "--setup-complete" was not found
            startupTime = new StartupTime();
        }
        SplashPanel.linfo.setText("Checking login...");
        if(PublicValues.config.get(ConfigValues.username.name).equals("")) {
            new LoginDialog().open(); //Show login dialog if no username is set
            startupTime = new StartupTime();
        }
        SplashPanel.linfo.setText("Add shutdown hook...");
        Runtime.getRuntime().addShutdownHook(hook.getRawThread()); //Gets executed when SpotifyXP is closing
        SplashPanel.linfo.setText("Creating api...");
        thread.start();
        api = new SpotifyAPI();
        SpotifyAPI.Player player = new SpotifyAPI.Player(api);
        past = true;
        SplashPanel.linfo.setText("Creating keylistener...");
        new KeyListener().start();
        SplashPanel.linfo.setText("Create advanced api key...");
        PublicValues.elevated = new SpotifyAPI.OAuthPKCE();
        SplashPanel.linfo.setText("Creating contentPanel...");
        ContentPanel panel = new ContentPanel(player, api);
        SplashPanel.linfo.setText("Starting background services...");
        new BackgroundService().start();
        DefThread t = new DefThread(new Runnable() {
            @Override
            public void run() {
                SplashPanel.linfo.setText("Invoking analytics...");
                new Analytics();
            }
        });
        t.start();
        SplashPanel.linfo.setText("Check updater...");
        Updater.UpdateInfo info = new Updater().updateAvailable();
        if(info.updateAvailable) {
            String version = info.version;
            ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
            new UpdaterDialog();
        }else{
            if(new Updater().isNightly()) {
                ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.nightly"));
                ContentPanel.feedbackupdaterdownloadbutton.setVisible(false);
            } else {
                ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.notavailable"));
            }
        }
        SplashPanel.linfo.setText("Showing startup time...");
        ConsoleLogging.info(PublicValues.language.translate("startup.info.took").replace("{}", startupTime.getMMSS()));
        SplashPanel.hide();
        panel.open();
        new HttpService();
        if(PublicValues.isSteamDeckMode) {
            new SteamDeckSupportModule();
        }
    }
}
