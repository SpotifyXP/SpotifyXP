package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.com.spotify.context.ContextTrackOuterClass;
import com.spotifyxp.deps.se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.spotifyxp.dialogs.LyricsDialog;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.graphics.Graphics;
import com.spotifyxp.history.PlaybackHistory;
import com.spotifyxp.listeners.PlayerListener;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.swingextension.*;
import com.spotifyxp.utils.*;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PlayerArea extends JPanel {
    public static JImagePanel playerimage;
    public static JScrollText playertitle;
    public static JLabel playerdescription;
    public static JImageButton playerplaypreviousbutton;
    public static JImageButton playerplaypausebutton;
    public static JImageButton playerplaynextbutton;
    public static JSlider playercurrenttime;
    public static JLabel playerplaytime;
    public static JLabel playerplaytimetotal;
    public static JSVGPanel playerareashufflebutton;
    public static JSVGPanel playerarearepeatingbutton;
    public static JSVGPanel playerarealyricsbutton;
    public static JSVGPanel playerareavolumeicon;
    public static JSlider playerareavolumeslider;
    public static JLabel playerareavolumecurrent;
    public static JSVGPanel heart;
    public static JSVGPanel historybutton;
    private static int playerareawidth = 0;
    private static int playerareaheight = 0;
    private boolean windowWasOpened = false;
    private static PlayerArea playerarea;
    private static LastPlayState lastPlayState;
    private static boolean doneLastParsing = false;

    public PlayerArea(JFrame frame) {
        playerarea = this;
        setBounds(72, 0, 565, 100);
        setLayout(null);
        playerareashufflebutton = new JSVGPanel();
        playerareashufflebutton.getJComponent().setBounds(510, 75, 20, 20);
        playerareashufflebutton.getJComponent().setBackground(frame.getBackground());
        // frame.add(playerareashufflebutton.getJComponent());
        playerarearepeatingbutton = new JSVGPanel();
        playerarearepeatingbutton.getJComponent().setBounds(540, 75, 20, 20);
        playerarearepeatingbutton.getJComponent().setBackground(frame.getBackground());
        add(playerarearepeatingbutton.getJComponent());
        playerareashufflebutton.getJComponent().addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (playerareashufflebutton.isFilled) {
                    PublicValues.shuffle = false;
                    InstanceManager.getPlayer().getPlayer().setShuffle(false);
                    try {
                        PublicValues.spotifyplayer.tracks(true).next.clear();
                        for (String s : Shuffle.before) {
                            PublicValues.spotifyplayer.addToQueue(s);
                        }
                    } catch (Exception e2) {
                        ConsoleLogging.Throwable(e2);
                        GraphicalMessage.openException(e2);
                    }
                    playerareashufflebutton.setImage(Graphics.SHUFFLE.getPath());
                    playerareashufflebutton.isFilled = false;
                } else {
                    PublicValues.shuffle = true;
                    InstanceManager.getPlayer().getPlayer().setShuffle(true);
                    Shuffle.makeShuffle();
                    playerareashufflebutton.isFilled = true;
                    playerareashufflebutton.setImage(Graphics.SHUFFLESELECTED.getPath());
                }
            }
        }));
        playerarearepeatingbutton.getJComponent().addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (playerarearepeatingbutton.isFilled) {
                    InstanceManager.getPlayer().getPlayer().setRepeat(false, false);
                    playerarearepeatingbutton.setImage(Graphics.REPEAT.getPath());
                    playerarearepeatingbutton.isFilled = false;
                } else {
                    InstanceManager.getPlayer().getPlayer().setRepeat(true, false);
                    playerarearepeatingbutton.isFilled = true;
                    playerarearepeatingbutton.setImage(Graphics.REPEATSELECTED.getPath());
                }
            }
        }));
        playerimage = new JImagePanel();
        playerimage.setBounds(10, 11, 78, 78);
        add(playerimage);
        playerarealyricsbutton = new JSVGPanel();
        playerarealyricsbutton.getJComponent().setBounds(280, 75, 14, 14);
        playerarealyricsbutton.getJComponent().setBackground(frame.getBackground());
        add(playerarealyricsbutton.getJComponent());
        playerarealyricsbutton.getJComponent().addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    if (PublicValues.lyricsDialog == null) {
                        PublicValues.lyricsDialog = new LyricsDialog();
                    }
                    if (playerarealyricsbutton.isFilled) {
                        PublicValues.lyricsDialog.close();
                        playerarealyricsbutton.setImage(Graphics.MICROPHONE.getPath());
                        playerarealyricsbutton.isFilled = false;
                    } else {
                        if (PublicValues.lyricsDialog.open(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri())) {
                            playerarealyricsbutton.setImage(Graphics.MICROPHONESELECTED.getPath());
                            playerarealyricsbutton.isFilled = true;
                        }
                    }
                } catch (NullPointerException e2) {
                    playerarealyricsbutton.setImage(Graphics.MICROPHONE.getPath());
                    playerarealyricsbutton.isFilled = false;
                }
            }
        }));
        playerareavolumeicon = new JSVGPanel();
        playerareavolumeicon.getJComponent().setBounds(306, 75, 14, 14);
        playerareavolumeicon.getJComponent().setBackground(frame.getBackground());
        add(playerareavolumeicon.getJComponent());
        playerareavolumecurrent = new JLabel();
        playerareavolumecurrent.setBounds(489, 75, 35, 14);
        add(playerareavolumecurrent);
        playerareavolumeslider = new JSlider();
        playerareavolumeslider.setBounds(334, 76, 145, 13);
        add(playerareavolumeslider);
        playerareavolumeslider.setForeground(PublicValues.globalFontColor);
        playerareavolumecurrent.setText("10");
        playerareavolumeslider.setMinimum(0);
        playerareavolumeslider.setMaximum(10);
        playerareavolumeslider.setValue(10);
        InstanceManager.getPlayer().getPlayer().setVolume(65536);
        playerareavolumeslider.addChangeListener(e -> {
            playerareavolumecurrent.setText(String.valueOf(playerareavolumeslider.getValue()));
            switch (playerareavolumeslider.getValue()) {
                case 0:
                    InstanceManager.getPlayer().getPlayer().setVolume(0);
                    playerareavolumeicon.setImage(Graphics.VOLUMEMUTE.getPath());
                    break;
                case 1:
                    InstanceManager.getPlayer().getPlayer().setVolume(6553);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 2:
                    InstanceManager.getPlayer().getPlayer().setVolume(13107);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 3:
                    InstanceManager.getPlayer().getPlayer().setVolume(19660);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 4:
                    InstanceManager.getPlayer().getPlayer().setVolume(26214);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 5:
                    InstanceManager.getPlayer().getPlayer().setVolume(32768);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 6:
                    InstanceManager.getPlayer().getPlayer().setVolume(39321);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 7:
                    InstanceManager.getPlayer().getPlayer().setVolume(45875);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 8:
                    InstanceManager.getPlayer().getPlayer().setVolume(52428);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 9:
                    InstanceManager.getPlayer().getPlayer().setVolume(58982);
                    playerareavolumeicon.setImage(Graphics.VOLUMEHALF.getPath());
                    break;
                case 10:
                    InstanceManager.getPlayer().getPlayer().setVolume(65536);
                    playerareavolumeicon.setImage(Graphics.VOLUMEFULL.getPath());
                    break;
            }
        });
        playertitle = new JScrollText(PublicValues.language.translate("ui.player.title"));
        playertitle.setBounds(109, 11, 168, 14);
        add(playertitle);
        playertitle.setForeground(PublicValues.globalFontColor);
        playerdescription = new JLabel(PublicValues.language.translate("ui.player.description"));
        playerdescription.setBounds(109, 40, 138, 20);
        add(playerdescription);
        playerdescription.setForeground(PublicValues.globalFontColor);
        playerplaypreviousbutton = new JImageButton();
        playerplaypreviousbutton.setBounds(287, 11, 70, 36);
        playerplaypreviousbutton.setColor(frame.getBackground());
        add(playerplaypreviousbutton);
        playerplaypausebutton = new JImageButton();
        playerplaypausebutton.setColor(frame.getBackground());
        playerplaypausebutton.setBounds(369, 11, 69, 36);
        playerplaypausebutton.addActionListener(e -> InstanceManager.getPlayer().getPlayer().playPause());
        add(playerplaypausebutton);
        playerplaynextbutton = new JImageButton();
        playerplaynextbutton.setColor(frame.getBackground());
        playerplaynextbutton.setBounds(448, 11, 69, 36);
        add(playerplaynextbutton);
        playercurrenttime = new JSlider();
        playercurrenttime.setValue(0);
        playercurrenttime.setBounds(306, 54, 200, 13);
        add(playercurrenttime);
        playerplaytime = new JLabel("00:00");
        playerplaytime.setHorizontalAlignment(SwingConstants.RIGHT);
        playerplaytime.setBounds(244, 54, 57, 14);
        add(playerplaytime);
        playerplaytime.setForeground(PublicValues.globalFontColor);
        playerplaytimetotal = new JLabel("00:00");
        playerplaytimetotal.setBounds(506, 54, 49, 14);
        add(playerplaytimetotal);
        playerplaytimetotal.setForeground(PublicValues.globalFontColor);
        heart = new JSVGPanel();
        heart.getJComponent().setBackground(frame.getBackground());
        heart.getJComponent().setBounds(525, 20, 24, 24);
        heart.getJComponent().addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (heart.isFilled) {
                    try {
                        InstanceManager.getSpotifyApi().removeUsersSavedTracks(Objects.requireNonNull(InstanceManager.getPlayer().getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]).build().execute();
                    }catch (IOException | ParseException | SpotifyWebApiException ex) {
                        throw new RuntimeException(ex);
                    }
                    heart.setImage(Graphics.HEART.getPath());
                    heart.isFilled = false;
                } else {
                    try {
                        InstanceManager.getSpotifyApi().saveTracksForUser(Objects.requireNonNull(InstanceManager.getPlayer().getPlayer().currentPlayable()).toSpotifyUri().split(":")[2]).build().execute();
                    }catch (IOException | ParseException | SpotifyWebApiException ex) {
                        throw new RuntimeException(ex);
                    }
                    heart.setImage(Graphics.HEARTFILLED.getPath());
                    heart.isFilled = true;
                }
            }
        }));
        heart.setImage(Graphics.HEART.getPath());
        add(heart.getJComponent());
        playercurrenttime.setForeground(PublicValues.globalFontColor);
        playercurrenttime.addChangeListener(e -> playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(playercurrenttime.getValue() * 1000L)));
        playercurrenttime.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                InstanceManager.getPlayer().getPlayer().pause();
                PlayerListener.pauseTimer = true;
            }
        }));
        playercurrenttime.addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                PlayerListener.pauseTimer = false;
                InstanceManager.getPlayer().getPlayer().seek(playercurrenttime.getValue() * 1000);
                InstanceManager.getPlayer().getPlayer().play();
            }
        }));
        playerplaynextbutton.addActionListener(e -> InstanceManager.getPlayer().getPlayer().next());
        playerplaypreviousbutton.addActionListener(new AsyncActionListener(e -> PublicValues.spotifyplayer.previous()));
        playercurrenttime.addChangeListener(e -> playerplaytime.setText(TrackUtils.getHHMMSSOfTrack(InstanceManager.getPlayer().getPlayer().time())));

        SplashPanel.linfo.setText("Creating playback history...");
        PublicValues.history = new PlaybackHistory();
        Events.subscribe(SpotifyXPEvents.trackNext.getName(), (Object... data) -> {
            if(PublicValues.spotifyplayer.currentPlayable() == null) return;
            if(!doneLastParsing) return;
            if(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri().split(":")[1].equals("track")) {
                try {
                    PublicValues.history.addSong(InstanceManager.getSpotifyApi().getTrack(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri().split(":")[2]).build().execute());
                } catch (SQLException | IOException | SpotifyWebApiException | ParseException e) {
                    ConsoleLogging.Throwable(e);
                }
            }
        });
        historybutton = new JSVGPanel();
        historybutton.setImage(Graphics.HISTORY.getPath());
        historybutton.getJComponent().setBounds(720, 50, 20, 20);
        historybutton.getJComponent().addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(historybutton.isFilled) {
                    historybutton.isFilled = false;
                    historybutton.setImage(Graphics.HISTORY.getPath());
                    PublicValues.history.dispose();
                }else{
                    historybutton.isFilled = true;
                    historybutton.setImage(Graphics.HISTORYSELECTED.getPath());
                    PublicValues.history.open();
                }
            }
        }));
        ContentPanel.frame.add(historybutton.getJComponent());

        JFrame dialog = new JFrame();
        addMouseListener(new AsyncMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    if (!dialog.isVisible()) {
                        playerareawidth = getWidth();
                        playerareaheight = getHeight();
                        windowWasOpened = true;
                        frame.getContentPane().remove(playerarea);
                        frame.repaint();
                        frame.getContentPane().repaint();
                        dialog.setTitle(PublicValues.language.translate("ui.player.dialog.title"));
                        dialog.setContentPane(playerarea);
                        dialog.setPreferredSize(new Dimension(338, 359));
                        dialog.setVisible(true);
                        dialog.pack();
                        changePlayerToWindowStyle();
                    } else {
                        windowWasOpened = false;
                        dialog.dispose();
                        setBounds(784 / 2 - playerareawidth / 2, 8, playerareawidth, playerareaheight);
                        restoreDefaultPlayerStyle();
                        PublicValues.contentPanel.add(playerarea);
                        frame.repaint();
                        PublicValues.contentPanel.repaint();
                    }
                }
            }
        }));
        dialog.setResizable(false);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                windowWasOpened = false;
                dialog.dispose();
                setBounds(784 / 2 - playerareawidth / 2, 8, playerareawidth, playerareaheight);
                restoreDefaultPlayerStyle();
                PublicValues.contentPanel.add(playerarea);
                frame.repaint();
                PublicValues.contentPanel.repaint();
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dialog.dispose();
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                if (windowWasOpened) {
                    dialog.setVisible(true);
                }
            }
        });

        Events.subscribe(SpotifyXPEvents.onFrameReady.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                if (new File(PublicValues.fileslocation, "play.state").exists()) {
                    parseLastPlayState();
                    try {
                        if (!lastPlayState.uri.isEmpty()) {
                            PlayerArea.playerplaytime.setText(lastPlayState.playtime);
                            PlayerArea.playerplaytimetotal.setText(lastPlayState.playtimetotal);
                            PlayerArea.playercurrenttime.setMaximum(Integer.parseInt(lastPlayState.playerslidermax));
                            PublicValues.spotifyplayer.load(lastPlayState.uri, false, PublicValues.shuffle);
                            if (!TrackUtils.isTrackLiked(lastPlayState.uri.split(":")[2])) {
                                PlayerArea.heart.isFilled = false;
                                PlayerArea.heart.setImage(Graphics.HEART.getPath());
                            } else {
                                PlayerArea.heart.isFilled = true;
                                PlayerArea.heart.setImage(Graphics.HEARTFILLED.getPath());
                            }
                            EventSubscriber subscriber = new EventSubscriber() {
                                @Override
                                public void run(Object... data) {
                                    PublicValues.spotifyplayer.seek(Integer.parseInt(lastPlayState.playerslider) * 1000);
                                    PlayerArea.playerareavolumeslider.setValue(Integer.parseInt(lastPlayState.playervolume));
                                    Events.unsubscribe(SpotifyXPEvents.playerLockRelease.getName(), this);
                                    doneLastParsing = true;
                                }
                            };
                            Events.subscribe(SpotifyXPEvents.playerLockRelease.getName(), subscriber);
                        }
                        if(!lastPlayState.queue.isEmpty()) {
                            try {
                                PublicValues.spotifyplayer.tracks(true).next.clear();
                                for(String s : lastPlayState.queue) {
                                    PublicValues.spotifyplayer.addToQueue(s);
                                }
                            }catch (Exception ignored) {
                                ConsoleLogging.warning("Failed to restore player queue");
                            }
                        }
                    } catch (Exception e) {
                        //Failed to load last play state! Dont notify user because its not that important
                    }
                }
            }
        });
    }

    void changePlayerToWindowStyle() {
        playerareashufflebutton.getJComponent().setBounds(20, 293, 20, 20);
        playerarearepeatingbutton.getJComponent().setBounds(280, 293, 20, 20);
        playertitle.setHorizontalAlignment(SwingConstants.CENTER);
        playerarealyricsbutton.getJComponent().setBounds(20, 232, 14, 14);
        playerdescription.setHorizontalAlignment(SwingConstants.CENTER);
        playerimage.setBounds(91, 11, 132, 130);
         playertitle.setBounds(10, 166, 301, 14);
        playerdescription.setBounds(10, 184, 300, 20);
        playerplaypreviousbutton.setBounds(47, 222, 70, 36);
        playerplaypausebutton.setBounds(129, 222, 69, 36);
        playerplaynextbutton.setBounds(208, 222, 69, 36);
        playercurrenttime.setBounds(62, 269, 200, 13);
        playerplaytime.setBounds(0, 269, 57, 14);
        playerplaytimetotal.setBounds(262, 269, 49, 14);
        playerareavolumeicon.getJComponent().setBounds(62, 293, 14, 14);
        playerareavolumeslider.setBounds(87, 293, 145, 13);
        playerareavolumecurrent.setBounds(242, 293, 35, 14);
        heart.getJComponent().setBounds(286, 229, 24, 24);
    }

    void restoreDefaultPlayerStyle() {
        playerarearepeatingbutton.getJComponent().setBounds(540, 75, 20, 20);
        playerareashufflebutton.getJComponent().setBounds(510, 75, 20, 20);
        playertitle.setHorizontalAlignment(SwingConstants.LEFT);
        playerdescription.setHorizontalAlignment(SwingConstants.LEFT);
        playerimage.setBounds(10, 11, 78, 78);
        playertitle.setBounds(109, 11, 168, 14);
        playerarealyricsbutton.getJComponent().setBounds(280, 75, 14, 14);
        playerdescription.setBounds(109, 40, 138, 20);
        playerplaypreviousbutton.setBounds(287, 11, 70, 36);
        playerplaypausebutton.setBounds(369, 11, 69, 36);
        playerplaynextbutton.setBounds(448, 11, 69, 36);
        playercurrenttime.setBounds(306, 54, 200, 13);
        playerplaytime.setBounds(244, 54, 57, 14);
        playerplaytimetotal.setBounds(506, 54, 49, 14);
        playerareavolumeicon.getJComponent().setBounds(306, 75, 14, 14);
        playerareavolumeslider.setBounds(334, 76, 145, 13);
        playerareavolumecurrent.setBounds(489, 75, 35, 14);
        heart.getJComponent().setBounds(525, 20, 24, 24);
    }

    public static void saveCurrentState() {
        try {
            Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri();
        } catch (Exception e) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(new File(PublicValues.fileslocation, "play.state"));
            StringBuilder queue = new StringBuilder();
            int counter = 0;
            for(ContextTrackOuterClass.ContextTrack t : PublicValues.spotifyplayer.tracks(true).next) {
                if(counter == 50) break;
                queue.append(t.getUri()).append("\n");
                counter++;
            }
            writer.write(Objects.requireNonNull(PublicValues.spotifyplayer.currentPlayable()).toSpotifyUri() + "\n" + PlayerArea.playercurrenttime.getValue() + "\n" + PlayerArea.playerplaytime.getText() + "\n" + PlayerArea.playerplaytimetotal.getText() + "\n" + PlayerArea.playercurrenttime.getMaximum() + "\n" + PlayerArea.playerareavolumecurrent.getText() + "\n" + StringUtils.replaceLast(queue.toString(), "\n", ""));
            writer.close();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            GraphicalMessage.openException(e);
        }
        try {
            new File(PublicValues.appLocation, "LOCK").delete();
        } catch (Exception e) {
            ConsoleLogging.Throwable(e);
        }
    }

    void parseLastPlayState() {
        try {
            LastPlayState state = new LastPlayState();
            File playstate = new File(PublicValues.fileslocation, "play.state");
            Scanner scan = new Scanner(playstate);
            int read = 0;
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                switch (read) {
                    case 0:
                        state.uri = data;
                        break;
                    case 1:
                        state.playerslider = data;
                        break;
                    case 2:
                        state.playtime = data;
                        break;
                    case 3:
                        state.playtimetotal = data;
                        break;
                    case 4:
                        state.playerslidermax = data;
                        break;
                    case 5:
                        state.playervolume = data;
                        break;
                }
                read++;
            }
            String out = IOUtils.toString(new FileInputStream(new File(PublicValues.fileslocation, "play.state")), Charset.defaultCharset());
            for(int i = 0; i < out.split("\n").length; i++) {
                if(i > 5) {
                    state.queue.add(out.split("\n")[i]);
                }
            }
            scan.close();
            lastPlayState = state;
        } catch (FileNotFoundException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class LastPlayState {
        public String uri;
        public String playtimetotal;
        public String playtime;
        public String playerslider;
        public String playerslidermax;
        public String playervolume;
        public final ArrayList<String> queue = new ArrayList<>();
    }
}
