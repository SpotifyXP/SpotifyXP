package com.spotifyxp.background;


import com.spotifyxp.dialogs.SystemTrayDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BackgroundService {
    public static SystemTrayDialog trayDialog;
    public void start() {
        try {
            trayDialog = new SystemTrayDialog();
            trayDialog.add(new ImageIcon(ImageIO.read(new Resources().readToInputStream("spotifyxp.png"))), "SpotifyXP");
            trayDialog.open(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ContentPanel.frame.setVisible(true);
                }
            });
        }
        catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
