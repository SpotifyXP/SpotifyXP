package com.spotifyxp.background;


import com.spotifyxp.dialogs.SystemTrayDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BackgroundService {
    public static SystemTrayDialog trayDialog;

    /**
     * Creates the tray icon
     */
    public void start() {
        try {
            trayDialog = new SystemTrayDialog();
            trayDialog.add(new ImageIcon(ImageIO.read(new Resources().readToInputStream("spotifyxp.png"))), "SpotifyXP");
            trayDialog.addEntry("Open", e -> ContentPanel.frame.setVisible(true));
            trayDialog.addEntry("Exit", e -> System.exit(0));
            trayDialog.open();
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
