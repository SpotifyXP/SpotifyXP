package com.spotifyxp.background;


import com.spotifyxp.PublicValues;
import com.spotifyxp.dialogs.SystemTrayDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.URLUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        startInternetListener();
    }

    public LastType lastType = LastType.INTERNET;

    public static enum LastType {
        INTERNET,
        NO_INTERNET;
    }

    public void startInternetListener() {
        Runnable internetListenerRunnable = new Runnable() {
            public void run() {
                try {
                    URL url = new URL("https://www.apple.com/library/test/success.html");
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    if(lastType!=LastType.INTERNET) {
                        BackgroundService.trayDialog.getTrayIcon().displayMessage(PublicValues.language.translate("message.connection.title"), PublicValues.language.translate("message.connection"), TrayIcon.MessageType.INFO);
                        ContentPanel.toggleNoInternet();
                    }
                    lastType = LastType.INTERNET;
                } catch (Exception e) {
                    if(lastType!=LastType.NO_INTERNET) {
                        BackgroundService.trayDialog.getTrayIcon().displayMessage(PublicValues.language.translate("message.noconnection.title"), PublicValues.language.translate("message.noconnection"), TrayIcon.MessageType.ERROR);
                        ContentPanel.toggleNoInternet();
                    }
                    lastType = LastType.NO_INTERNET;
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(internetListenerRunnable, 0, 120, TimeUnit.MILLISECONDS);
    }
}
