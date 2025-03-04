package com.spotifyxp.pip;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.ctxmenu.ContextMenu;
import com.spotifyxp.deps.com.spotify.canvaz.CanvazOuterClass;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.mercury.MercuryClient;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.panels.PlayerArea;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.swingextension.JImageButton;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class PiPPlayer extends JFrame {
    public static JImagePanel songImage;
    private int pX, pY;
    private boolean isResizing = false, isMoving = false;
    private Rectangle resizingRect, northWestRect, northEastRect, southWestRect, southEastRect;
    private int resizingRectSpacing = 10;
    private int resizeDirection = Cursor.DEFAULT_CURSOR;

    public static JLayeredPane container;
    public static JImageButton playPause;
    public static JImageButton closeButton;
    public static JPanel controls;
    public static JImageButton nextButton;
    public static JImageButton previousButton;
    public static JPanel videoContainer;

    public static String pausePath = "/icons/playerpausedark.svg";
    public static String playPath = "/icons/playerplaydark.svg";
    public static String closePath = "/icons/closedark.svg";
    public static String nextPath = "/icons/playerplaynextdark.svg";
    public static String previousPath = "/icons/playerplaypreviousdark.svg";

    public static int initialWindowSize = 280;
    public static int buttonSize = 30;

    public static File cachePath;

    public static ContextMenu ctxMenu;

    void resizeComponents() {
        for(Component component : container.getComponents()) {
            if(component.getName() != null && component.getName().equals("ResizeRect")) {
                component.setBounds(resizingRect);
                continue;
            }
            component.setBounds(0, 0, getWidth(), getHeight());
        }
        closeButton.setBounds(resizingRect.width - buttonSize, 0, buttonSize, buttonSize);
        previousButton.setBounds(0, resizingRect.height - buttonSize, buttonSize, buttonSize);
        playPause.setBounds(resizingRect.width / 2  - buttonSize / 2, resizingRect.height - buttonSize, buttonSize, buttonSize);
        nextButton.setBounds(resizingRect.width - buttonSize, resizingRect.height - buttonSize, buttonSize, buttonSize);
    }

    MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)) {
                ctxMenu.showAt(container, e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
            if(SwingUtilities.isRightMouseButton(me)) {
                return;
            }

            pX = me.getX();
            pY = me.getY();

            if (northWestRect.contains(me.getPoint())) {
                isResizing = true;
                resizeDirection = Cursor.NW_RESIZE_CURSOR;
            } else if (northEastRect.contains(me.getPoint())) {
                isResizing = true;
                resizeDirection = Cursor.NE_RESIZE_CURSOR;
            } else if (southWestRect.contains(me.getPoint())) {
                isResizing = true;
                resizeDirection = Cursor.SW_RESIZE_CURSOR;
            } else if (southEastRect.contains(me.getPoint())) {
                isResizing = true;
                resizeDirection = Cursor.SE_RESIZE_CURSOR;
            } else if (resizingRect.contains(me.getPoint())) {
                isMoving = true;
                setCursor(Cursor.MOVE_CURSOR);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isResizing = false;
            isMoving = false;
            resizeDirection = Cursor.DEFAULT_CURSOR;
            setCursor(Cursor.DEFAULT_CURSOR);
            recalculateRects(false);
            resizeComponents();
            revalidate();
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            controls.setVisible(true);
            revalidate();
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(contains(e.getPoint())) return;
            controls.setVisible(false);
            revalidate();
            repaint();
        }
    };

    MouseMotionListener mouseMotionListener = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent me) {
            int dx = me.getX() - pX;
            int dy = me.getY() - pY;

            if (isResizing) {
                int newX = getX();
                int newY = getY();
                int newWidth = getWidth();
                int newHeight = getHeight();

                switch (resizeDirection) {
                    case Cursor.NW_RESIZE_CURSOR:
                        newX += dx;
                        newY += dy;
                        newWidth -= dx;
                        newHeight -= dy;
                        break;
                    case Cursor.NE_RESIZE_CURSOR:
                        newY += dy;
                        newWidth += dx;
                        newHeight -= dy;
                        break;
                    case Cursor.SW_RESIZE_CURSOR:
                        newX += dx;
                        newWidth -= dx;
                        newHeight += dy;
                        break;
                    case Cursor.SE_RESIZE_CURSOR:
                        newWidth += dx;
                        newHeight += dy;
                        break;
                }

                if (newWidth > 100 && newHeight > 100) {
                    setBounds(newX, newY, newWidth, newHeight);
                    pX = me.getX();
                    pY = me.getY();
                }
            } else if (isMoving) {
                setLocation(getLocation().x + dx, getLocation().y + dy);
            }
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            Component at = controls.getComponentAt(me.getPoint());
            if(at != null) {
                if(!(at instanceof JPanel)) {
                    setCursor(Cursor.DEFAULT_CURSOR);
                    return;
                }
            }
            if (northWestRect.contains(me.getPoint())) {
                setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            } else if (northEastRect.contains(me.getPoint())) {
                setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
            } else if (southWestRect.contains(me.getPoint())) {
                setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
            } else if (southEastRect.contains(me.getPoint())) {
                setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            } else if (resizingRect.contains(me.getPoint())) {
                setCursor(Cursor.MOVE_CURSOR);
            } else {
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        }
    };

    public PiPPlayer() {
        recalculateRects(true);

        setBackground(Color.BLACK);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PublicValues.vlcPlayer.release();
                dispose();
            }
        });
        setAlwaysOnTop(true);
        setUndecorated(true);
        setPreferredSize(new Dimension(initialWindowSize, initialWindowSize));
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseMotionListener);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        if(!PublicValues.config.getBoolean(ConfigValues.cache_disabled.name)) {
            cachePath = new File(PublicValues.appLocation, "cvnscache");
            if(!cachePath.exists()) {
                if(!cachePath.mkdir()) {
                    ConsoleLogging.error("Failed to create cvnscache directory");
                    PublicValues.contentPanel.remove(PlayerArea.canvasPlayerButton.getJComponent());
                }
            }
        }

        container = new JLayeredPane();
        container.setBackground(Color.BLACK);
        setContentPane(container);

        ctxMenu = new ContextMenu();
        ctxMenu.addItem(PublicValues.language.translate("pip.ctxmenu.item1"), new Runnable() {
            @Override
            public void run() {
                String buttonHeight = JOptionPane.showInputDialog(PublicValues.language.translate("pip.ctxmenu.item1.message"));
                if(buttonHeight.isEmpty()) {
                    return;
                }
                try {
                    buttonSize = Integer.parseInt(buttonHeight);
                    resizeComponents();
                }catch (NumberFormatException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
        });


        songImage = new JImagePanel();
        songImage.setBackground(Color.BLACK);
        songImage.setSize(initialWindowSize, initialWindowSize);
        container.add(songImage, JLayeredPane.DEFAULT_LAYER);


        videoContainer = new JPanel();
        //videoContainer.setVisible(false);
        videoContainer.setLayout(new BorderLayout());
        container.add(videoContainer, JLayeredPane.PALETTE_LAYER);


        controls = new JPanel();
        controls.setLayout(null);
        controls.setBounds(resizingRect);
        controls.setVisible(false);
        controls.setName("ResizeRect");
        controls.setOpaque(false);
        controls.setBackground(new Color(0, 0, 0, 0));
        container.add(controls, JLayeredPane.MODAL_LAYER);

        closeButton = new JImageButton();
        closeButton.setBorderPainted(false);
        closeButton.setImage(new Resources().readToInputStream(closePath));
        closeButton.setColor(Color.WHITE);
        closeButton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }));
        closeButton.setBounds(resizingRect.width / 2 - buttonSize / 2, 0, buttonSize, buttonSize);
        controls.add(closeButton);


        previousButton = new JImageButton();
        previousButton.setBorderPainted(false);
        previousButton.setImage(new Resources().readToInputStream(previousPath));
        previousButton.setColor(Color.WHITE);
        previousButton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstanceManager.getSpotifyPlayer().previous();
            }
        }));
        previousButton.setBounds(0, resizingRect.height - buttonSize, buttonSize, buttonSize);
        controls.add(previousButton);


        playPause = new JImageButton();
        playPause.setImage(new Resources().readToInputStream(playPath));
        playPause.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(InstanceManager.getSpotifyPlayer().isPaused()) {
                    InstanceManager.getSpotifyPlayer().play();
                }else {
                    InstanceManager.getSpotifyPlayer().pause();
                }
            }
        }));
        playPause.setBorderPainted(false);
        playPause.setColor(Color.WHITE);
        playPause.setBounds(resizingRect.width / 2  - buttonSize / 2, resizingRect.height - buttonSize, buttonSize, buttonSize);
        controls.add(playPause);


        nextButton = new JImageButton();
        nextButton.setBorderPainted(false);
        nextButton.setImage(new Resources().readToInputStream(nextPath));
        nextButton.setColor(Color.WHITE);
        nextButton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstanceManager.getSpotifyPlayer().next();
            }
        }));
        nextButton.setBounds(resizingRect.width - buttonSize, resizingRect.height - buttonSize, buttonSize, buttonSize);
        controls.add(nextButton);

        Events.subscribe(SpotifyXPEvents.playerresume.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                if(isVisible()) {
                    playPause.setImage(new Resources().readToInputStream(pausePath));
                }
            }
        });

        Events.subscribe(SpotifyXPEvents.playerpause.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                if(isVisible()) {
                    playPause.setImage(new Resources().readToInputStream(playPath));
                }
            }
        });

        Events.subscribe(SpotifyXPEvents.playerLockRelease.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                if(isVisible()) {
                    songImage.setImage(PlayerArea.playerImage.getImageStream());
                }
            }
        });
    }

    private void recalculateRects(boolean preInit) {
        int windowWidth = getWidth();
        int windowHeight = getHeight();
        if(preInit) {
            windowWidth = initialWindowSize;
            windowHeight = initialWindowSize;
        }
        resizingRect = new Rectangle(resizingRectSpacing, resizingRectSpacing, windowWidth - (resizingRectSpacing * 2), windowHeight - (resizingRectSpacing * 2));
        northWestRect = new Rectangle(0, 0, resizingRectSpacing, resizingRectSpacing);
        northEastRect = new Rectangle(windowWidth - resizingRectSpacing, 0, resizingRectSpacing, resizingRectSpacing);
        southWestRect = new Rectangle(0, windowHeight - resizingRectSpacing, resizingRectSpacing, resizingRectSpacing);
        southEastRect = new Rectangle(windowWidth - resizingRectSpacing, windowHeight - resizingRectSpacing, resizingRectSpacing, resizingRectSpacing);
    }

    @Override
    public void close() {
        if(PublicValues.vlcPlayer.isVideoPlaybackEnabled()) {
            videoContainer.remove(PublicValues.vlcPlayer.getComponent());
            PublicValues.vlcPlayer.getComponent().getComponent(0).removeMouseListener(mouseAdapter);
            PublicValues.vlcPlayer.getComponent().getComponent(0).removeMouseMotionListener(mouseMotionListener);
            Events.unsubscribe(SpotifyXPEvents.trackNext.getName(), onNextTrack);
            Events.unsubscribe(SpotifyXPEvents.playerpause.getName(), onPause);
            Events.unsubscribe(SpotifyXPEvents.playerresume.getName(), onPlay);
        }
        super.close();
    }

    EventSubscriber onPause = new EventSubscriber() {
        @Override
        public void run(Object... data) {
            if(PublicValues.vlcPlayer.wasReleased()) return;
            if(!PublicValues.vlcPlayer.isPlaying()) return;
            PublicValues.vlcPlayer.pause();
        }
    };

    EventSubscriber onPlay = new EventSubscriber() {
        @Override
        public void run(Object... data) {
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

    private void loadCanvas(String uri) {
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
                videoContainer.setVisible(true);
                PublicValues.vlcPlayer.play(new File(cachePath, convertUrlToName(cvnsUrl)).getAbsolutePath());
            } else {
                String url = PublicValues.session.api().getCanvases(CanvazOuterClass.EntityCanvazRequest.newBuilder()
                        .addEntities(CanvazOuterClass.EntityCanvazRequest.Entity.newBuilder()
                                .setEntityUri(uri)
                                .buildPartial())
                        .build()).getCanvases(0).getUrl();
                videoContainer.setVisible(true);
                PublicValues.vlcPlayer.play(url);
            }
        } catch (IndexOutOfBoundsException ignored) {
            // No canvas for track
            ConsoleLogging.info("No canvas available for track");
            videoContainer.setVisible(false);
        } catch (IOException | MercuryClient.MercuryException e) {
            throw new RuntimeException(e);
        }
    }

    EventSubscriber onNextTrack = new EventSubscriber() {
        @Override
        public void run(Object... data) {
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
    public void open() {
        super.open();
        resizeComponents();
        setLocation(
                getGraphicsConfiguration().getBounds().width - getWidth(),
                getGraphicsConfiguration().getBounds().height - getHeight()
        );
        if(PublicValues.vlcPlayer.isVideoPlaybackEnabled()) {
            if(PlayerArea.canvasPlayer != null && PlayerArea.canvasPlayer.isVisible()) {
                PlayerArea.canvasPlayer.setVisible(false);
            }
            PublicValues.vlcPlayer.init(this::close);
            PublicValues.vlcPlayer.setLooping(true);
            PublicValues.vlcPlayer.getComponent().getComponent(0).addMouseListener(mouseAdapter);
            PublicValues.vlcPlayer.getComponent().getComponent(0).addMouseMotionListener(mouseMotionListener);
            videoContainer.add(PublicValues.vlcPlayer.getComponent(), BorderLayout.CENTER);
            revalidate();
            repaint();
            loadCanvas(Objects.requireNonNull(InstanceManager.getSpotifyPlayer().currentMetadata()).id.toSpotifyUri());
            Events.subscribe(SpotifyXPEvents.trackNext.getName(), onNextTrack);
            Events.subscribe(SpotifyXPEvents.playerpause.getName(), onPause);
            Events.subscribe(SpotifyXPEvents.playerresume.getName(), onPlay);
        }
        if(InstanceManager.getSpotifyPlayer().isPaused()) {
            playPause.setImage(new Resources().readToInputStream(playPath));
        }else{
            playPause.setImage(new Resources().readToInputStream(pausePath));
        }
        songImage.setImage(PlayerArea.playerImage.getImageStream());
    }
}