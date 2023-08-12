package com.spotifyxp;


import com.spotifyxp.audio.Quality;
import com.spotifyxp.beamngintegration.HttpService;
import com.spotifyxp.deps.mslinks.ShellLink;
import com.spotifyxp.deps.mslinks.ShellLinkException;
import com.spotifyxp.deps.mslinks.ShellLinkHelper;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.injector.Injector;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.setup.Setup;
import com.spotifyxp.stabilizer.GlobalExceptionHandler;
import com.spotifyxp.support.LinuxSupportModule;
import com.spotifyxp.support.MacOSSupportModule;
import com.spotifyxp.support.SteamDeckSupportModule;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.threading.DefThread;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.*;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.io.Resource;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Convert2Lambda")
public class Initiator {
    static SpotifyAPI api = null;
    public static StartupTime startupTime;
    static DefThread hook = new DefThread(ContentPanel::saveCurrentState);
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
        PublicValues.logger.setColored(!System.getProperty("os.name").toLowerCase().contains("win"));
        PublicValues.logger.setShowTime(false);
        SplashPanel.linfo.setText("Loading Extensions...");
        new Injector().autoInject();
        SplashPanel.linfo.setText("Parsing arguments...");
        PublicValues.argParser.parseArguments(args);
        SplashPanel.linfo.setText("Checking debugging...");
        ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
        modules.setColored(!System.getProperty("os.name").toLowerCase().contains("win"));
        modules.setShowTime(false);
        SplashPanel.linfo.setText("Detecting operating system...");
        if(!System.getProperty("os.name").toLowerCase().contains("win")) {
            if(System.getProperty("os.name").toLowerCase().toLowerCase().contains("mac")) {
                SplashPanel.linfo.setText("Found MacOS! Applying MacOS patch...");
                new MacOSSupportModule();
            }else {
                SplashPanel.linfo.setText("Found Linux! Applying Linux patch...");
                new LinuxSupportModule();
            }
        }
        SplashPanel.linfo.setText("Checking required folders...");
        SplashPanel.linfo.setText("Initializing config...");
        PublicValues.config = new Config();
        SplashPanel.linfo.setText("Setting up globalexceptionhandler...");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        SplashPanel.linfo.setText("Storing program arguments...");
        PublicValues.args = args;
        SplashPanel.linfo.setText("Init Language...");
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setNoAutoFindLanguage(libLanguage.Language.getCodeFromName(PublicValues.config.get(ConfigValues.language.name)));
        SplashPanel.linfo.setText("Parsing audio quality info...");
        try {
            PublicValues.quality = Quality.valueOf(PublicValues.config.get(ConfigValues.audioquality.name));
        }catch (IllegalArgumentException exception) {
            //This should not happen but when it happens don't crash SpotifyXP
            PublicValues.quality = Quality.NORMAL;
        }
        SplashPanel.linfo.setText("Checking setup...");
        if(!PublicValues.foundSetupArgument) {
            new Setup();
            startupTime = new StartupTime();
        }
        SplashPanel.linfo.setText("Init Themes...");
        ThemeLoader loader = new ThemeLoader();
        try {
            loader.loadTheme(PublicValues.config.get(ConfigValues.theme.name));
        } catch (ThemeLoader.UnknownThemeException e) {
            ConsoleLogging.warning("Unknown Theme: '" + PublicValues.config.get(ConfigValues.theme.name) + "'! Trying to load theme differently");
            try {
                loader.tryLoadTheme(PublicValues.config.get(ConfigValues.theme.name));
            }catch (Exception e2) {
                ConsoleLogging.error("Loading no theme");
            }
        }
        try {
            Files.copy(new Resources().readToInputStream("SpotifyXP-Updater.jar"), Paths.get(PublicValues.appLocation + "/SpotifyXP-Updater.jar"), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
        try {
            new File(PublicValues.appLocation, "LOCK").createNewFile();
            new File(PublicValues.appLocation, "LOCK").deleteOnExit();
        }catch (Exception e) {
            ExceptionDialog.open(e);
            ConsoleLoggingModules.Throwable(e);
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
        SplashPanel.linfo.setText("Check updater...");
        Updater.UpdateInfo info = new Updater().updateAvailable();
        if(info.updateAvailable) {
            String version = info.version;
            ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
            new Updater().invoke();
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
