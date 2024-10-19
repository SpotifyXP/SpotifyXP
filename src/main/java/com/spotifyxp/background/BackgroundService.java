package com.spotifyxp.background;


import com.spotifyxp.PublicValues;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.lib.libDetect;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.tray.ExtendedSystemTray;
import com.spotifyxp.tray.SystemTrayDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class BackgroundService implements ExtendedSystemTray {
    /**
     * Holds the trayDialog after start() was called
     */
    public static SystemTrayDialog trayDialog;
    private JTextField titleAndText;

    /**
     * Creates the tray icon
     */
    public void start() {
        try {
            trayDialog = new SystemTrayDialog();
            trayDialog.setExtendedTrayIcon(this, e -> {
                ContentPanel.frame.setVisible(true);
                ContentPanel.frame.requestFocus();
            }, new ImageIcon(ImageIO.read(new Resources().readToInputStream("spotifyxp.png"))).getImage(), "SpotifyXP");
            trayDialog.openExtended();
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }

    EventSubscriber onNextTrack = new EventSubscriber() {
        @Override
        public void run(Object... data) {
            titleAndText.setText(InstanceManager.getPlayer().getPlayer().currentMetadata().getName() + " - "
                    + InstanceManager.getPlayer().getPlayer().currentMetadata().getArtist());
        }
    };

    @Override
    public void onInit(JDialog dialog) {
        dialog.setModal(true);
        if(PublicValues.osType != libDetect.OSType.Windows) {
            dialog.dispose();
            return;
        }
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        try {
            titleAndText = new JTextField();
            titleAndText.setEditable(false);

            Events.subscribe(SpotifyXPEvents.trackNext.getName(), onNextTrack);

            titleAndText.setText(InstanceManager.getPlayer().getPlayer().currentMetadata().getName() + " - "
                    + InstanceManager.getPlayer().getPlayer().currentMetadata().getArtist());

            textPanel.add(titleAndText, BorderLayout.CENTER);
        }catch (NullPointerException e) {
            ConsoleLogging.Throwable(e);
            titleAndText = new JTextField("N/A");
            titleAndText.setEditable(false);

            textPanel.add(titleAndText,BorderLayout.CENTER);
        }

        contentPanel.add(textPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        JButton nextButton = new JButton("Next"); //ToDo: Translate
        JButton previousButton = new JButton("Previous"); //ToDo: Translate
        JButton playPauseButton = new JButton("P/P"); //ToDo: Translate

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstanceManager.getPlayer().getPlayer().next();
            }
        });

        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstanceManager.getPlayer().getPlayer().playPause();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstanceManager.getPlayer().getPlayer().previous();
            }
        });

        controlPanel.add(nextButton, BorderLayout.EAST);
        controlPanel.add(playPauseButton, BorderLayout.CENTER);
        controlPanel.add(previousButton, BorderLayout.WEST);

        contentPanel.add(controlPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
    }

    @Override
    public void onOpen() {
        if(PublicValues.osType != libDetect.OSType.Windows) {
            ContentPanel.frame.setVisible(true);
            ContentPanel.frame.requestFocus();
        }
    }

    @Override
    public void onClose() {
        Events.unsubscribe(SpotifyXPEvents.trackNext.getName(), onNextTrack);
        titleAndText = null;
    }
}
