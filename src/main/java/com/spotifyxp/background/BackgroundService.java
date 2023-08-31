package com.spotifyxp.background;


import com.spotifyxp.PublicValues;
import com.spotifyxp.dialogs.SystemTrayDialog;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.ApplicationUtils;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.URLUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackgroundService {
    public static SystemTrayDialog trayDialog;
    public void start() {
        try {
            trayDialog = new SystemTrayDialog();
            trayDialog.add(new ImageIcon(ImageIO.read(new Resources().readToInputStream("spotifyxp.png"))), "SpotifyXP");
            trayDialog.open(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    ContentPanel.frame.setVisible(true);
                }
            });
        }
        catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
