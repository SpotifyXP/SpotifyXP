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
import de.werwolf2303.javasetuptool.Setup;
import de.werwolf2303.javasetuptool.components.AcceptComponent;
import de.werwolf2303.javasetuptool.components.HTMLComponent;
import de.werwolf2303.javasetuptool.components.InstallProgressComponent;
import de.werwolf2303.javasetuptool.uninstaller.Uninstaller;
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
        PublicValues.logger.setColored(false);
        PublicValues.logger.setShowTime(false);
        SplashPanel.linfo.setText("Loading Extensions...");
        new Injector().autoInject();
        SplashPanel.linfo.setText("Parsing arguments...");
        PublicValues.argParser.parseArguments(args);
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
        if(!new File(PublicValues.fileslocation).exists()) {
            if(!new File(PublicValues.fileslocation).mkdir()) {
                ConsoleLogging.changeName("SpotifyAPI");
                ConsoleLogging.error("Failed to create files");
            }
        }
        if(!new File(PublicValues.appLocation).exists()) {
            if(!new File(PublicValues.appLocation).mkdir()) {
                ConsoleLogging.error("Failed to create app location");
            }
        }
        SplashPanel.linfo.setText("Initializing config...");
        PublicValues.config = new Config();
        SplashPanel.linfo.setText("Setting up globalexceptionhandler...");
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        SplashPanel.linfo.setText("Storing program arguments...");
        PublicValues.args = args;
        SplashPanel.linfo.setText("Detecting debugging...");
        if(new File("pom.xml").exists()) {
            PublicValues.debug = true;
            ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
            modules.setColored(!System.getProperty("os.name").toLowerCase().contains("xp"));
            modules.setShowTime(false);
            PublicValues.logger.setColored(!System.getProperty("os.name").toLowerCase().contains("xp"));
            PublicValues.foundSetupArgument = true;
        }
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
            //Diskspace 60mb
            Setup setup = new Setup();
            ArrayList<String> files = new ArrayList<>();
            ArrayList<String> folders = new ArrayList<>();
            AcceptComponent thirdparty = new AcceptComponent();
            thirdparty.load(new Resources().readToString("setup/thirdparty.html"));
            InstallProgressComponent install = new InstallProgressComponent();
            if(!PublicValues.isMacOS && !PublicValues.isLinux) {
                install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                        .setType(InstallProgressComponent.FileOperationTypes.CREATEDIR)
                        .setTo(PublicValues.appLocation));
                install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                        .setType(InstallProgressComponent.FileOperationTypes.COPYSTREAM)
                        .setFrom(new Resources().readToInputStream("spotifyxp.ico"))
                        .setTo(PublicValues.appLocation + "/spotifyxp.ico"));
                try {
                    install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                            .setFrom(Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                            .setTo(PublicValues.appLocation + "/SpotifyXP.jar")
                            .setType(InstallProgressComponent.FileOperationTypes.COPY));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                        .setType(InstallProgressComponent.FileOperationTypes.CUSTOM).setCustom(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ShellLink shellLink = new ShellLink();
                                    shellLink.setIconLocation(PublicValues.appLocation + "/SpotifyXP.ico");
                                    shellLink.setCMDArgs("--setup-complete");
                                    ShellLinkHelper helper = new ShellLinkHelper(shellLink);
                                    helper.setLocalTarget("C", PublicValues.appLocation.replace("C:\\", "") + "/SpotifyXP.jar");
                                    helper.saveTo(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk");
                                } catch (IOException | ShellLinkException e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                        .setType(InstallProgressComponent.FileOperationTypes.COPY)
                        .setFrom(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk")
                        .setTo(PublicValues.startmenupath + "/SpotifyXP.lnk"));
                folders.add(PublicValues.fileslocation);
                files.add(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk");
                files.add(PublicValues.startmenupath + "/SpotifyXP.lnk");
            }else{
                if(PublicValues.isLinux) {
                    install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                            .setType(InstallProgressComponent.FileOperationTypes.CREATEDIR)
                            .setTo(PublicValues.appLocation));
                    install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                            .setType(InstallProgressComponent.FileOperationTypes.COPYSTREAM)
                            .setFrom(new Resources().readToInputStream("spotifyxp.ico"))
                            .setTo(PublicValues.appLocation + "/spotifyxp.ico"));
                    try {
                        install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                                .setFrom(Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                                .setTo(PublicValues.appLocation + "/SpotifyXP.jar")
                                .setType(InstallProgressComponent.FileOperationTypes.COPY));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                            .setType(InstallProgressComponent.FileOperationTypes.CUSTOM).setCustom(new Runnable() {
                                @Override
                                public void run() {
                                    LinuxAppUtil util = new LinuxAppUtil("SpotifyXP");
                                    util.setVersion(PublicValues.version);
                                    util.setComment("Listen to Spotify");
                                    util.setPath(PublicValues.appLocation);
                                    util.setExecutableLocation("java -jar SpotifyXP.jar --setup-complete");
                                    util.setIconlocation(PublicValues.appLocation + "/spotifyxp.ico");
                                    util.setCategories("Java", "Music");
                                    util.create();
                                }
                            }));
                    folders.add(PublicValues.fileslocation);
                    files.add(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk");
                    files.add(PublicValues.startmenupath + "/SpotifyXP.lnk");
                    files.add("/usr/share/applications/" + "SpotifyXP.desktop");
                    files.add(System.getProperty("user.home") + "/Schreibtisch" +  "/SpotifyXP.desktop");
                    files.add(System.getProperty("user.home") + "/Desktop" + "/SpotifyXP.desktop");
                }else{
                    if(PublicValues.isMacOS) {
                        install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                                .setType(InstallProgressComponent.FileOperationTypes.CREATEDIR)
                                .setTo(PublicValues.appLocation));
                        install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                                .setType(InstallProgressComponent.FileOperationTypes.COPYSTREAM)
                                .setFrom(new Resources().readToInputStream("spotifyxp.ico"))
                                .setTo(PublicValues.appLocation + "/spotifyxp.ico"));
                        try {
                            install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                                    .setFrom(Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                                    .setTo(PublicValues.appLocation + "/SpotifyXP.jar")
                                    .setType(InstallProgressComponent.FileOperationTypes.COPY));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        install.addFileOperation(new InstallProgressComponent.FileOperationBuilder()
                                .setType(InstallProgressComponent.FileOperationTypes.CUSTOM).setCustom(new Runnable() {
                                    @Override
                                    public void run() {
                                        MacOSAppUtil util = new MacOSAppUtil("SpotifyXP");
                                        util.setIcon("spotifyxp.icns");
                                        util.setExecutableLocation(PublicValues.appLocation + "/SpotifyXP.jar");
                                        util.create();
                                    }
                                }));
                        folders.add(PublicValues.fileslocation);
                        files.add(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk");
                        folders.add(System.getProperty("user.home") + "/Applications/SpotifyXP.app");
                    }
                }
            }
            final boolean[] hold = {true};
            setup.open(new Setup.SetupBuilder()
                    .setProgramVersion(PublicValues.version)
                    .setProgramName("SpotifyXP")
                    .setProgramImage(new Resources().readToInputStream("spotifyxp.png"))
                    .addComponent(thirdparty)
                    .setInstallComponent(install)
                    .setOnFinish(new Runnable() {
                        @Override
                        public void run() {
                            PublicValues.foundSetupArgument = true;
                            hold[0] = false;
                            try {
                                new Uninstaller().buildUninstaller(files, folders, "SpotifyXP", PublicValues.version, PublicValues.appLocation + File.separator + "uninstaller.xml");
                            } catch (ParserConfigurationException e) {
                                ConsoleLogging.Throwable(e);
                                GraphicalMessage.bug("Failed to create uninstaller");
                            }
                        }
                    })
                    .build()
            );
            while(hold[0]) {
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                } catch (InterruptedException ignored) {
                }
            }
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
                GraphicalMessage.bug("Can't load any theme");
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
