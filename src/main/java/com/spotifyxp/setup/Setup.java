package com.spotifyxp.setup;

import com.spotifyxp.Initiator;
import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.FileUtils;
import com.spotifyxp.utils.Resources;
import mslinks.ShellLink;
import mslinks.ShellLinkException;
import mslinks.ShellLinkHelper;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
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
        public Content() {
            setLayout(null);

            setupnextbutton = new JButton("Next");
            setupnextbutton.setBounds(395, 338, 89, 23);
            add(setupnextbutton);

            setuppreviousbutton = new JButton("Previous");
            setuppreviousbutton.setBounds(296, 338, 89, 23);
            add(setuppreviousbutton);

            setupcancelbutton = new JButton("Cancel");
            setupcancelbutton.setBounds(197, 338, 89, 23);
            add(setupcancelbutton);

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

        void showAnalytics() {
            setupcontent.setText(new Resources().readToString("setup/analytics.html"));
        }

        void showFileInfo() {
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

        void beginInstall() {
            try {
                progressBar.setVisible(true);
                //Create the SpotifyXP directory
                new File(PublicValues.appLocation).mkdir();
                copyInputStreamToFile(new Resources().readToInputStream("spotifyxp.ico"), new File(PublicValues.appLocation + "/spotifyxp.ico"));
                progressBar.setValue(25);
                //Get the path of the current Jar File
                String jarPath = Initiator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                jarPath = jarPath.replaceFirst("/", "");
                progressBar.setValue(50);
                //Now copy the file there
                Files.copy(Paths.get(jarPath), Paths.get(PublicValues.appLocation + "/SpotifyXP.jar"), REPLACE_EXISTING);
                progressBar.setValue(75);
                //Now create the shortcut
                ShellLink shellLink = new ShellLink();
                shellLink.setIconLocation(PublicValues.appLocation + "/SpotifyXP.ico");
                shellLink.setCMDArgs("--setup-complete");
                ShellLinkHelper helper = new ShellLinkHelper(shellLink);
                helper.setLocalTarget("C", PublicValues.appLocation.replace("C:\\", "") + "/SpotifyXP.jar");
                helper.saveTo(System.getProperty("user.home") + "/Desktop/SpotifyXP.lnk");
                progressBar.setValue(100);
                PublicValues.foundSetupArgument = true;
                switchToNext();
            } catch (URISyntaxException | IOException | ShellLinkException e) {
                ConsoleLogging.Throwable(e);
            }
        }

        void switchToNext() {
            switch(setupState) {
                case WELCOME:
                    setupState = SetupState.THIRD_PARTY_LICENSES;
                    showThirdPartyLicenses();
                    break;
                case THIRD_PARTY_LICENSES:
                    setupState = SetupState.ANALYTICS;
                    showAnalytics();
                    break;
                case ANALYTICS:
                    setupState = SetupState.FILE_INFO;
                    showFileInfo();
                    break;
                case FILE_INFO:
                    setupState = SetupState.COMPLETE;
                    showComplete();
                    break;
                case COMPLETE:
                    setupframe.setVisible(false);
            }
        }
        void switchToPrevious() {
            switch(setupState) {
                case WELCOME:
                    break;
                case THIRD_PARTY_LICENSES:
                    setupState = SetupState.WELCOME;
                    showWelcome();
                    break;
                case ANALYTICS:
                    setupState = SetupState.THIRD_PARTY_LICENSES;
                    showThirdPartyLicenses();
                    break;
                case FILE_INFO:
                    setupState = SetupState.ANALYTICS;
                    showAnalytics();
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
        while(setupframe.isVisible()) {
            try {
                Thread.sleep(999);
            } catch (InterruptedException e) {
                ConsoleLogging.Throwable(e);
            }
        }
    }

}
