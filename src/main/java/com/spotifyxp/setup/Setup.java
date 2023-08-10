package com.spotifyxp.setup;

import com.spotifyxp.Initiator;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.LinuxAppUtil;
import com.spotifyxp.utils.MacOSAppUtil;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.deps.mslinks.ShellLink;
import com.spotifyxp.deps.mslinks.ShellLinkException;
import com.spotifyxp.deps.mslinks.ShellLinkHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.io.IOUtils.DEFAULT_BUFFER_SIZE;

public class Setup {
    public static JFrame setupframe = new JFrame();

    public Setup() {
        displaySetup();
    }

    public static SetupState setupState = SetupState.WELCOME;
    Content content;

    public Content getContent() {
        return content;
    }

    static class Content extends JPanel {
        public JButton setupnextbutton;
        public JButton setuppreviousbutton;
        public JButton setupcancelbutton;
        public JTextPane setupcontent;
        public JScrollPane pane;
        public JProgressBar progressBar;
        public JButton setupfinishbutton;
        public JButton dontacceptbutton;

        public Content() {
            setLayout(null);

            dontacceptbutton = new JButton();
            dontacceptbutton.setBounds(100, 338, 89, 23);
            add(dontacceptbutton);

            dontacceptbutton.setVisible(false);

            setupnextbutton = new JButton("Next");
            setupnextbutton.setBounds(395, 338, 89, 23);
            add(setupnextbutton);

            setuppreviousbutton = new JButton("Previous");
            setuppreviousbutton.setBounds(296, 338, 89, 23);
            add(setuppreviousbutton);

            setupcancelbutton = new JButton("Cancel");
            setupcancelbutton.setBounds(197, 338, 89, 23);
            add(setupcancelbutton);

            SplashPanel.hide();

            setupcancelbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            setupcontent = new JTextPane();

            pane = new JScrollPane(setupcontent);
            pane.setBounds(0, 0, 484, 338);
            add(pane);

            progressBar = new JProgressBar();
            progressBar.setValue(100);
            progressBar.setBounds(10, 338, 177, 23);
            add(progressBar);

            progressBar.setForeground(Color.green);
            progressBar.setVisible(false);

            setupfinishbutton = new JButton("Finish");
            setupfinishbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchToNext();
                }
            });
            setupfinishbutton.setBounds(395, 338, 89, 23);
            add(setupfinishbutton);

            setupcontent.setEditable(false);
            setupcontent.setContentType("text/html");

            setupfinishbutton.setVisible(false);

            setupnextbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchToNext();
                }
            });

            setuppreviousbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchToPrevious();
                }
            });

            showWelcome();
        }

        void showWelcome() {
            setupcontent.setText(new Resources().readToString("setup/welcome.html"));
        }

        void showThirdPartyLicenses() {
            setupcontent.setText(new Resources().readToString("setup/thirdparty.html"));
            setupcontent.setCaretPosition(0);
        }

        void showFileInfo() {
            dontacceptbutton.setVisible(false);
            setupcontent.setText(new Resources().readToString("setup/fileinfo.html"));
            beginInstall();
            setupnextbutton.setVisible(false);
            setuppreviousbutton.setVisible(false);
        }

        void showComplete() {
            setupcontent.setText(new Resources().readToString("setup/complete.html"));
            setupfinishbutton.setVisible(true);
            setupnextbutton.setVisible(false);
            setuppreviousbutton.setVisible(false);
            setupcancelbutton.setVisible(false);
        }

        void copyInputStreamToFile(InputStream inputStream, File file)
                throws IOException {

            // append = false
            try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
                int read;
                byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }

        }

        void beginInstallMacOS() {
            try {
                progressBar.setVisible(true);
                //Create the SpotifyXP directory
                if (!new File(PublicValues.appLocation).exists()) {
                    new File(PublicValues.appLocation).mkdir();
                }
                copyInputStreamToFile(new Resources().readToInputStream("spotifyxp.ico"), new File(PublicValues.appLocation + "/spotifyxp.ico"));
                progressBar.setValue(25);
                //Get the path of the current Jar File
                String jarPath = Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                progressBar.setValue(50);
                //Now copy the file there
                Files.copy(Paths.get(jarPath), Paths.get(PublicValues.appLocation + "/SpotifyXP.jar"), REPLACE_EXISTING);
                progressBar.setValue(75);
                //Now create the shortcut
                MacOSAppUtil util = new MacOSAppUtil("SpotifyXP");
                util.setIcon("spotifyxp.icns");
                util.setExecutableLocation(PublicValues.appLocation + "/SpotifyXP.jar");
                util.create();
                progressBar.setValue(100);
                PublicValues.foundSetupArgument = true;
                switchToNext();
            } catch (URISyntaxException | IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }

        void beginInstallLinux() {
            try {
                progressBar.setVisible(true);
                //Create the SpotifyXP directory
                if (!new File(PublicValues.appLocation).exists()) {
                    new File(PublicValues.appLocation).mkdir();
                }
                copyInputStreamToFile(new Resources().readToInputStream("spotifyxp.ico"), new File(PublicValues.appLocation + "/spotifyxp.ico"));
                progressBar.setValue(25);
                //Get the path of the current Jar File
                String jarPath = Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                progressBar.setValue(50);
                //Now copy the file there
                Files.copy(Paths.get(jarPath), Paths.get(PublicValues.appLocation + "/SpotifyXP.jar"), REPLACE_EXISTING);
                progressBar.setValue(75);
                //Now create the shortcut
                LinuxAppUtil util = new LinuxAppUtil("SpotifyXP");
                util.setVersion(PublicValues.version);
                util.setComment("Listen to Spotify");
                util.setPath(PublicValues.appLocation);
                util.setExecutableLocation("java -jar SpotifyXP.jar --setup-complete");
                util.setIconlocation(PublicValues.appLocation + "/spotifyxp.ico");
                util.setCategories("Java", "Music");
                util.create();
                progressBar.setValue(100);
                PublicValues.foundSetupArgument = true;
                switchToNext();
            } catch (URISyntaxException | IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }

        void beginInstallWindows() {
            try {
                progressBar.setVisible(true);
                //Create the SpotifyXP directory
                if (!new File(PublicValues.appLocation).exists()) {
                    new File(PublicValues.appLocation).mkdir();
                }
                copyInputStreamToFile(new Resources().readToInputStream("spotifyxp.ico"), new File(PublicValues.appLocation + "/spotifyxp.ico"));
                progressBar.setValue(25);
                //Get the path of the current Jar File
                String jarPath = Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                jarPath = jarPath.replaceFirst("/", "");
                progressBar.setValue(50);
                //Now copy the file there
                if (!PublicValues.appLocation.startsWith("/")) {
                    Files.copy(Paths.get(jarPath), Paths.get(PublicValues.appLocation + "/SpotifyXP.jar"), REPLACE_EXISTING);
                }
                progressBar.setValue(75);
                //Now create the shortcut
                if (!PublicValues.appLocation.startsWith("/")) {
                    ShellLink shellLink = new ShellLink();
                    shellLink.setIconLocation(PublicValues.appLocation + "/SpotifyXP.ico");
                    shellLink.setCMDArgs("--setup-complete");
                    ShellLinkHelper helper = new ShellLinkHelper(shellLink);
                    helper.setLocalTarget("C", PublicValues.appLocation.replace("C:\\", "") + "/SpotifyXP.jar");
                    helper.saveTo(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk");
                }
                try {
                    if (new File(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk").exists()) {
                        Files.copy(Paths.get(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk"), Paths.get(PublicValues.startmenupath + "/SpotifyXP.lnk"), REPLACE_EXISTING);
                    }
                } catch (Exception r) {
                    r.printStackTrace();
                }
                progressBar.setValue(100);
                PublicValues.foundSetupArgument = true;
                switchToNext();
            } catch (URISyntaxException | IOException | ShellLinkException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }

        void beginInstall() {
            if (PublicValues.isMacOS) {
                beginInstallMacOS();
            } else {
                if (PublicValues.isLinux) {
                    beginInstallLinux();
                } else {
                    beginInstallWindows();
                }
            }
        }

        void switchToNext() {
            switch (setupState) {
                case WELCOME:
                    setupState = SetupState.THIRD_PARTY_LICENSES;
                    showThirdPartyLicenses();
                    break;
                case THIRD_PARTY_LICENSES:
                    setupState = SetupState.WELCOME;
                    showWelcome();
                    break;
                case FILE_INFO:
                    setupState = SetupState.COMPLETE;
                    showComplete();
                    break;
                case COMPLETE:
                    SplashPanel.frame.setVisible(true);
                    setupframe.setVisible(false);
            }
        }

        void switchToPrevious() {
            switch (setupState) {
                case WELCOME:
                    break;
                case THIRD_PARTY_LICENSES:
                    setupState = SetupState.WELCOME;
                    showWelcome();
                    break;
                case FILE_INFO:
                    setupState = SetupState.THIRD_PARTY_LICENSES;
                    showComplete();
                    break;
            }
        }
    }

    void displaySetup() {
        content = new Content();
        setupframe.setTitle("SpotifyXP Setup for version: " + PublicValues.version);
        setupframe.setPreferredSize(new Dimension(500, 400));
        setupframe.getContentPane().add(getContent());
        setupframe.setVisible(true);
        setupframe.setResizable(false);
        setupframe.pack();
        while (setupframe.isVisible()) {
            try {
                Thread.sleep(999);
            } catch (InterruptedException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
    }
}
