package com.spotifyxp.video;

import com.spotifyxp.PublicValues;
import com.spotifyxp.dummy.DummyCanvasPlayer;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.StringUtils;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.*;
import uk.co.caprica.vlcj.player.base.State;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class CanvasPlayer {
    private MediaPlayerFactory factory;
    private final Rectangle bounds = new Rectangle();

    private EmbeddedMediaPlayer mediaPlayer;

    private Window window;

    private VideoSurface videoSurface;

    private JFrame frame;

    private JPanel referencePanel;
    private boolean trackValid = false;
    public CanvasPlayer() {
        try {
            frame = new JFrame("SpotifyXP - Canvas");
            frame.setPreferredSize(new Dimension(294, 526));
            frame.setBackground(Color.black);
            frame.getContentPane().setBackground(Color.white);
            try {
                frame.setIconImage(ImageIO.read(new Resources(false).readToInputStream("spotifyxp.png")));
            } catch (IOException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
            referencePanel = new JPanel();
            referencePanel.setBackground(Color.black);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(referencePanel);
            factory = new MediaPlayerFactory();
            mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
            window = new Window(frame);
            window.setBackground(Color.black);
            videoSurface = factory.videoSurfaces().newVideoSurface(window);
            mediaPlayer.videoSurface().set(videoSurface);
            window.setBounds(0, 0, 294, 526);
            window.setIgnoreRepaint(true);
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new FlowLayout());
            frame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        hide();
                    }
                });
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentMoved(ComponentEvent e) {syncVideoSurface();
                    }
                });
            frame.setResizable(false);
            mediaPlayer.controls().setRepeat(true);
            frame.pack();
        }catch (Exception e) {
            PublicValues.canvasPlayer = new DummyCanvasPlayer(false);
            ConsoleLogging.warning("Couldn't create canvasplayer! Is VLC installed?");
        }
    }

    void syncVideoSurface() {
        if (frame.isVisible()) {
            referencePanel.getBounds(bounds);
            bounds.setLocation(referencePanel.getLocationOnScreen());
            window.setBounds(bounds);
        }
    }

    public void switchMedia(String url) {
        if(!url.contains("https")) {
            //No canvas available for track
            trackValid = false;
            return;
        }
        window.repaint();
        trackValid = true;
        mediaPlayer.media().prepare(url);
        syncVideoSurface();
    }

    public void play() {
        if(!trackValid) return;
        mediaPlayer.controls().play();
    }

    public void stop() {
        if(!trackValid) return;
        mediaPlayer.controls().stop();
    }

    public void show() {
        frame.setVisible(true);
        window.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
        window.setVisible(false);
    }

    public boolean isShown() {
        return (frame.isVisible() && window.isVisible());
    }

    public void pause() {
        if(!trackValid) return;
        mediaPlayer.controls().pause();
    }

    private void release() {
        mediaPlayer.controls().stop();
        mediaPlayer.release();
        factory.release();
    }
}
