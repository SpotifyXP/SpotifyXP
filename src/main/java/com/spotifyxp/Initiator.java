package com.spotifyxp;


import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.audio.Quality;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLoggingModules;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.setup.Setup;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.DoubleArrayList;
import com.spotifyxp.api.SpotifyAPI;
import com.spotifyxp.background.BackgroundService;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dialogs.LoginDialog;
import com.spotifyxp.listeners.KeyListener;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.StartupTime;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("Convert2Lambda")
public class Initiator {
    static SpotifyAPI api = null;
    static StartupTime startupTime;
    public static void main(String[] args) {
        startupTime = new StartupTime();
        PublicValues.args = args;
        PublicValues.logger.setColored(false);
        PublicValues.logger.setShowTime(false);
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.language.setAutoFindLanguage();
        if(!new File(PublicValues.fileslocation).exists()) {
            if(!new File(PublicValues.fileslocation).mkdir()) {
                ConsoleLogging.changeName("SpotifyAPI");
                ConsoleLogging.error(PublicValues.language.translate("error.configuration.failedcreate"), 39);
            }
        }
        if(new File("pom.xml").exists()) {
            PublicValues.debug = true;
            ConsoleLoggingModules modules = new ConsoleLoggingModules("Module");
            modules.setColored(false);
            modules.setShowTime(false);
            PublicValues.foundSetupArgument = true;
        }
        PublicValues.config = new Config();
        switch(PublicValues.config.get(ConfigValues.theme.name)) {
            case "QUAQUA":
                PublicValues.theme = Theme.QuaQua;
                break;
            case "MACOSDARK":
                PublicValues.theme = Theme.MacOSDark;
                break;
            case "MACOSLIGHT":
                PublicValues.theme = Theme.MacOSLight;
                break;
            case "LIGHT":
                PublicValues.theme = Theme.LIGHT;
                break;
            case "WINDOWS":
                PublicValues.theme = Theme.WINDOWS;
                break;
            default:
                PublicValues.theme = Theme.DARK;
                break;
        }
        try {
            PublicValues.quality = Quality.valueOf(PublicValues.config.get(ConfigValues.audioquality.name));
        }catch (IllegalArgumentException exception) {
            //This should not happen but when it happens don't crash SpotifyXP
            PublicValues.quality = Quality.NORMAL;
        }
        if(new File(PublicValues.fileslocation + "/" + "LOCKED").exists()) {
            JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.startup.alreadystarted"), "SpotifyXP", JOptionPane.OK_CANCEL_OPTION);
            System.exit(0);
        }
        switch (PublicValues.theme) {
            case DARK:
                try {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } catch (UnsupportedLookAndFeelException e) {
                    ConsoleLogging.Throwable(e);
                }
                break;
            case LIGHT:
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } catch (UnsupportedLookAndFeelException e) {
                    ConsoleLogging.Throwable(e);
                }
                break;
            case WINDOWS:
                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                } catch (UnsupportedLookAndFeelException e) {
                    ConsoleLogging.Throwable(e);
                }
                break;
            case QuaQua:
                try {
                    UIManager.setLookAndFeel(new QuaquaLookAndFeel());
                } catch (UnsupportedLookAndFeelException e) {
                    ConsoleLogging.Throwable(e);
                }
                break;
        }
        try {
            if (args[0].equals("--setup-complete")) {
                PublicValues.foundSetupArgument = true;
            }
        }catch (ArrayIndexOutOfBoundsException ioe) {
            if(!new File("pom.xml").exists()) {
                new Setup();
            }
        }
        if(!PublicValues.foundSetupArgument) {
            new Setup();
        }
        new SplashPanel().show();
        try {
            if(!new File(PublicValues.fileslocation + "/" + "LOCKED").createNewFile()) {
                ConsoleLogging.error(PublicValues.language.translate("startup.error.lockcreate"));
            }
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        if(PublicValues.config.get(ConfigValues.username.name).equals("")) {
            new LoginDialog().open();
        }
        Thread hook = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!new File(PublicValues.fileslocation + "/" + "LOCKED").delete()) {
                    ConsoleLogging.error(PublicValues.language.translate("startup.error.lockdelete"));
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(hook);
        api = new SpotifyAPI();
        SpotifyAPI.Player player = new SpotifyAPI.Player(api);
        new KeyListener().start();
        PublicValues.elevated = new SpotifyAPI.OAuthPKCE();
        ContentPanel panel = new ContentPanel(player, api);
        new BackgroundService().start();
        panel.open();
        new Analytics();
        SplashPanel.hide();
        DoubleArrayList updater = new Updater().updateAvailable();
        if(Boolean.parseBoolean(updater.getFirst(0).toString())) {
            String version = ((GitHubAPI.Release)updater.getSecond(0)).version;
            ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.available") + version);
            JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.updater.available") + version, "Updater", JOptionPane.OK_CANCEL_OPTION);
        }else{
            if(Double.parseDouble((((GitHubAPI.Release) updater.getSecond(0)).version.replace("v", "")))<Double.parseDouble(PublicValues.version.replace("v", ""))) {
                ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.nightly"));
                ContentPanel.feedbackupdaterdownloadbutton.setVisible(false);
            } else {
                ContentPanel.feedbackupdaterversionfield.setText(PublicValues.language.translate("ui.updater.notavailable"));
            }
        }
        ConsoleLogging.info("SpotifyXP needed " + startupTime.getHHMMSS() + " to start");
    }
}
