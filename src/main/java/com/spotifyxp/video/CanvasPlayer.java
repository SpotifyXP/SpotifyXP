package com.spotifyxp.video;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.com.spotify.canvaz.CanvazOuterClass;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.panels.PlayerArea;
import com.spotifyxp.swingextension.JFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class CanvasPlayer extends JFrame {
    private File cachePath;

    public CanvasPlayer() {
        setTitle("SpotifyXP - Canvas"); // ToDo: Translate
        setPreferredSize(new Dimension(290, 460));
        Events.subscribe(SpotifyXPEvents.trackNext.getName(), onNextTrack);
        Events.subscribe(SpotifyXPEvents.playerpause.getName(), onPause);
        Events.subscribe(SpotifyXPEvents.playerresume.getName(), onPlay);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PlayerArea.canvasPlayerButton.isFilled = false;
                PlayerArea.canvasPlayerButton.setImage(Graphics.VIDEO.getPath());
                PublicValues.vlcPlayer.stop();
                PublicValues.vlcPlayer.release();
            }
        });
        if(!PublicValues.config.getBoolean(ConfigValues.cache_disabled.name)) {
            cachePath = new File(PublicValues.appLocation, "cvnscache");
            if(!cachePath.exists()) {
                if(!cachePath.mkdir()) {
                    ConsoleLogging.error("Failed to create cvnscache directory");
                    PublicValues.contentPanel.remove(PlayerArea.canvasPlayerButton.getJComponent());
                }
            }
        }
    }

    EventSubscriber onPause = new EventSubscriber() {
        @Override
        public void run(Object... data) {
            if(!isVisible()) return;
            if(PublicValues.vlcPlayer.wasReleased()) return;
            if(!PublicValues.vlcPlayer.isPlaying()) return;
            PublicValues.vlcPlayer.pause();
        }
    };

    EventSubscriber onPlay = new EventSubscriber() {
        @Override
        public void run(Object... data) {
            if(!isVisible()) return;
            if(PublicValues.vlcPlayer.wasReleased()) return;
            if(PublicValues.vlcPlayer.isPlaying()) return;
            PublicValues.vlcPlayer.resume();
        }
    };

    private String convertUrlToName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    void clearCache() throws NullPointerException{
        for(File file : Objects.requireNonNull(cachePath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".mp4");
            }
        }))) {
            if(!file.delete()) {
                ConsoleLogging.warning("Failed to remove file in cvnscache");
            }
        }
    }

    public void loadCanvas(String uri) {
        try {
            if(!PublicValues.config.getBoolean(ConfigValues.cache_disabled.name)) {
                clearCache();
                String cvnsUrl = PublicValues.session.api().getCanvases(CanvazOuterClass.EntityCanvazRequest.newBuilder()
                        .addEntities(CanvazOuterClass.EntityCanvazRequest.Entity.newBuilder()
                                .setEntityUri(uri)
                                .buildPartial())
                        .build()).getCanvases(0).getUrl();
                try (BufferedInputStream in = new BufferedInputStream(new URL(cvnsUrl).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(new File(cachePath, convertUrlToName(cvnsUrl)));) {
                    byte[] dataBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                PublicValues.vlcPlayer.play(new File(cachePath, convertUrlToName(cvnsUrl)).getAbsolutePath());
            } else {
                PublicValues.vlcPlayer.play(PublicValues.session.api().getCanvases(CanvazOuterClass.EntityCanvazRequest.newBuilder()
                        .addEntities(CanvazOuterClass.EntityCanvazRequest.Entity.newBuilder()
                                .setEntityUri(uri)
                                .buildPartial())
                        .build()).getCanvases(0).getUrl());
            }
        } catch (IndexOutOfBoundsException ignored) {
            // No canvas for track
            ConsoleLogging.info("No canvas available for track");
        } catch (IOException | MercuryClient.MercuryException e) {
            throw new RuntimeException(e);
        }
    }

    EventSubscriber onNextTrack = new EventSubscriber() {
        @Override
        public void run(Object... data) {
            if(!isVisible()) return;
            if(PublicValues.vlcPlayer.wasReleased()) return;
            PublicValues.vlcPlayer.stop();
            MetadataWrapper metadataWrapper = InstanceManager.getSpotifyPlayer().currentMetadata();
            if (metadataWrapper == null || metadataWrapper.id == null) {
                return;
            }
            if(!metadataWrapper.isTrack()) {
                // Canvases are only available for tracks
                return;
            }
            loadCanvas(metadataWrapper.id.toSpotifyUri());
        }
    };

    @Override
    public void close() {
        remove(PublicValues.vlcPlayer.getComponent());
        super.close();
    }

    @Override
    public void open() throws NullPointerException {
        add(PublicValues.vlcPlayer.getComponent());
        PublicValues.vlcPlayer.init(this::close);
        PublicValues.vlcPlayer.setLooping(true);
        super.open();
        loadCanvas(Objects.requireNonNull(InstanceManager.getSpotifyPlayer().currentMetadata()).id.toSpotifyUri());
    }
}
