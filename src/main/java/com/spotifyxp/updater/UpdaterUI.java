package com.spotifyxp.updater;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.JFrame;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class UpdaterUI extends JFrame {
    private JPanel contents;
    private JButton updateButton;
    private JProgressBar progress;
    private JLabel progresslabel;
    private JButton cancelButton;
    private JTextPane changelog;
    private boolean allowClosure = false;
    private Updater.UpdateInfo updateInfo;
    private boolean updateDone = false;
    private boolean disableUpdateFunc = false;

    public UpdaterUI() throws IOException {
        $$$setupUI$$$();
        setLocation(ContentPanel.frame.getLocation());
        setContentPane(contents);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle(PublicValues.language.translate("updater.availableui.title"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (disableUpdateFunc) {
                    System.out.println("She closing mate");
                    e.getWindow().dispose();
                    return;
                }
                if (allowClosure) System.exit(0);
            }
        });

        updateButton.setText(PublicValues.language.translate("updater.availableui.updateButton"));
        updateButton.addActionListener(e -> {
            if (disableUpdateFunc) {
                try {
                    Updater.invoke(updateInfo);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }
            allowClosure = false;
            updateButton.setEnabled(false);
            cancelButton.setEnabled(false);
            Thread downloadThread = new Thread(() -> {
                download();
                cancelButton.setEnabled(true);
                cancelButton.setText(PublicValues.language.translate("updater.availableui.cancelButtonDone"));
                updateButton.setVisible(false);
                updateDone = true;
            });
            downloadThread.start();
        });

        cancelButton.setText(PublicValues.language.translate("updater.availableui.cancelButton"));
        cancelButton.addActionListener(e -> {
            if (disableUpdateFunc) {
                dispose();
                return;
            }
            if (updateDone) {
                ProcessBuilder builder = new ProcessBuilder(
                        "java",
                        "-jar",
                        new File(PublicValues.appLocation, "SpotifyXP.jar").getAbsolutePath()
                );
                try {
                    builder.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
            System.exit(0);
        });

        progresslabel.setText(PublicValues.language.translate("updater.availableui.progresslabel"));

        changelog.setEditable(false);
        changelog.setContentType("text/html");
    }

    private void download() {
        try {
            int dotState = 0;
            progresslabel.setText(PublicValues.language.translate("updater.availableui.updating"));
            progress.setMaximum(100000);
            URL url = new URL(updateInfo.url);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();
            BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(PublicValues.appLocation, "SpotifyXP.jar"));
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);
                progress.setValue(currentProgress);
                switch (dotState) {
                    case 0:
                        progresslabel.setText(PublicValues.language.translate("updater.availableui.updating") + ".");
                        dotState++;
                        break;
                    case 1:
                        progresslabel.setText(PublicValues.language.translate("updater.availableui.updating") + "..");
                        dotState++;
                        break;
                    case 2:
                        progresslabel.setText(PublicValues.language.translate("updater.availableui.updating") + "...");
                        dotState = 0;
                        break;
                }
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (SocketTimeoutException socketTimeoutException) {
            download();
        } catch (Exception exception) {
            progress.setForeground(Color.red);
            progresslabel.setText(PublicValues.language.translate("updater.availableui.failed").replace("%s", exception.getMessage()));
        }
        progresslabel.setText(PublicValues.language.translate("updater.availableui.done"));
    }

    public CompletableFuture<Boolean> openWithoutUpdateFunctionality(Updater.UpdateInfo updateInfo) throws IOException {
        CompletableFuture<Boolean> usersChoiceFuture = new CompletableFuture<>();
        disableUpdateFunc = true;
        this.updateInfo = updateInfo;
        progress.setVisible(false);
        progresslabel.setVisible(false);
        changelog.setText(GitHubAPI.getCommitMessage(updateInfo.commit_id));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                synchronized (usersChoiceFuture) {
                    usersChoiceFuture.complete(true);
                }
            }
        });
        super.open();
        return usersChoiceFuture;
    }

    public void open(Updater.UpdateInfo updateInfo) throws IOException {
        changelog.setText(GitHubAPI.getCommitMessage(updateInfo.commit_id));
        this.updateInfo = updateInfo;
        super.open();
    }

    private void createUIComponents() {
        contents = new JPanel();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contents.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contents.setPreferredSize(new Dimension(450, 235));
        progresslabel = new JLabel();
        Font progresslabelFont = this.$$$getFont$$$(null, Font.BOLD, -1, progresslabel.getFont());
        if (progresslabelFont != null) progresslabel.setFont(progresslabelFont);
        progresslabel.setText("Update available! Click 'start update' to update");
        contents.add(progresslabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 12), null, null, 0, false));
        progress = new JProgressBar();
        contents.add(progress, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 20), null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contents.add(panel1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        panel1.add(cancelButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 40), null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Start update");
        panel1.add(updateButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 40), null, 0, false));
        changelog = new JTextPane();
        contents.add(changelog, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contents;
    }

}
