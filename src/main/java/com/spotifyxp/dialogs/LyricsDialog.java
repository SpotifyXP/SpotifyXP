package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.swingextension.RAWTextArea;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import org.json.JSONException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

import static com.spotifyxp.panels.PlayerArea.playerarealyricsbutton;

public class LyricsDialog extends RAWTextArea {
    final JFrame frame = new JFrame("SpotifyXP - Song Lyrics");
    UnofficialSpotifyAPI.Lyrics lyrics;
    final RAWTextArea area = this;
    final JScrollPane pane = new JScrollPane(area);
    int oldw = 0;
    int oldh = 0;
    String uri;

    boolean spotifyMode = true;

    public void setSpotifyModeActive() {
        spotifyMode = true;
    }

    public void setSpotifyModeDisabled() {
        spotifyMode = false;
    }

    public boolean open(String uri) {
        if(!spotifyMode) {
            area.setText("");
            try {
                if (frame.isVisible()) {
                    lyrics = new UnofficialSpotifyAPI(Factory.getSpotifyApi().getAccessToken()).getLyrics(uri);
                } else {
                    lyrics = new UnofficialSpotifyAPI(Factory.getSpotifyApi().getAccessToken()).getLyrics(uri);
                    frame.add(pane, BorderLayout.CENTER);
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            if (PublicValues.theme.isLight()) {
                                playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonedark.svg"));
                            } else {
                                playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonewhite.svg"));
                            }
                            playerarealyricsbutton.isFilled = false;
                        }
                    });
                    try {
                        frame.setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
                    } catch (Exception e) {
                        ConsoleLogging.Throwable(e);
                        if (PublicValues.config.getString(ConfigValues.hideExceptions.name).equals("false")) {
                            GraphicalMessage.openException(e);
                        }
                    }
                    ContextMenu menu = new ContextMenu(area);
                    menu.addItem(PublicValues.language.translate("ui.general.copy"), area::copyText);
                    frame.setPreferredSize(new Dimension(ContentPanel.frame.getWidth() / 2, ContentPanel.frame.getHeight() / 2));
                    frame.setVisible(true);
                    frame.pack();
                }

                for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                    area.append(line.words + "\n");
                }
                return true;
            } catch (JSONException e) {
                return false;
            }
        }else{
            area.setColorModeActive();
            try {
                if (frame.isVisible()) {
                    lyrics = new UnofficialSpotifyAPI(Factory.getSpotifyApi().getAccessToken()).getLyrics(uri);
                    //New lyrics
                } else {
                    lyrics = new UnofficialSpotifyAPI(Factory.getSpotifyApi().getAccessToken()).getLyrics(uri);
                    frame.add(pane, BorderLayout.CENTER);
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            if (PublicValues.theme.isLight()) {
                                playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonedark.svg"));
                            } else {
                                playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonewhite.svg"));
                            }
                            playerarealyricsbutton.isFilled = false;
                            Events.unsubscribe(SpotifyXPEvents.playerSeekedBackwards.getName(), seekedBackwards());
                            Events.unsubscribe(SpotifyXPEvents.playerSeekedForwards.getName(), seekedForwards());
                        }
                    });
                    try {
                        frame.setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
                    } catch (Exception e) {
                        ConsoleLogging.Throwable(e);
                        if (PublicValues.config.getString(ConfigValues.hideExceptions.name).equals("false")) {
                            GraphicalMessage.openException(e);
                        }
                    }
                    ContextMenu menu = new ContextMenu(area);
                    menu.addItem(PublicValues.language.translate("ui.general.copy"), area::copyText);
                    frame.setPreferredSize(new Dimension(ContentPanel.frame.getWidth() / 2, ContentPanel.frame.getHeight() / 2));
                    frame.setVisible(true);
                    frame.pack();
                }
                coloredLines.clear();
                for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                    append(line.words, PublicValues.globalFontColor, line.startTimeMs);
                }
                for(ColoredLyricsLine line : new ArrayList<>(coloredLines)) {
                    if(line.startTimeMS < Factory.getPlayer().getPlayer().time()) {
                        coloredLines.remove(0);
                    }
                }
                try {
                    coloredLines.set(0, new ColoredLyricsLine(
                            PublicValues.globalFontColor.brighter().brighter(),
                            coloredLines.get(0).getTextContent(),
                            coloredLines.get(0).getStartTimeMS()
                    ));
                }catch (IndexOutOfBoundsException ignored) {
                }
                repaint();
                Events.subscribe(SpotifyXPEvents.playerSeekedBackwards.getName(), seekedBackwards());
                Events.subscribe(SpotifyXPEvents.playerSeekedForwards.getName(), seekedForwards());
                this.uri = uri;
                return true;
            }catch (JSONException e) {
                return false;
            }
        }
    }

    Runnable seekedForwards() {
        return new Runnable() {
            @Override
            public void run() {
                triggerRefresh();
            }
        };
    }

    Runnable seekedBackwards() {
        return new Runnable() {
            @Override
            public void run() {
                lyrics = new UnofficialSpotifyAPI(Factory.getSpotifyApi().getAccessToken()).getLyrics(uri);
                coloredLines.clear();
                for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                    append(line.words, PublicValues.globalFontColor, line.startTimeMs);
                }
                for(ColoredLyricsLine line : new ArrayList<>(coloredLines)) {
                    if(line.startTimeMS < Factory.getPlayer().getPlayer().time()) {
                        coloredLines.remove(0);
                    }
                }
                try {
                    coloredLines.set(0, new ColoredLyricsLine(
                            PublicValues.globalFontColor.brighter().brighter(),
                            coloredLines.get(0).getTextContent(),
                            coloredLines.get(0).getStartTimeMS()
                    ));
                }catch (IndexOutOfBoundsException ignored) {
                }
                repaint();
            }
        };
    }

    public void close() {
        Events.unsubscribe(SpotifyXPEvents.playerSeekedBackwards.getName(), seekedBackwards());
        Events.unsubscribe(SpotifyXPEvents.playerSeekedForwards.getName(), seekedForwards());
        frame.setVisible(false);
    }

    public void triggerRefresh() {
        if(lyrics == null) {
            return;
        }
        if(!spotifyMode) {
            int counter = 0;
            for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                try {
                    if (line.startTimeMs < PublicValues.spotifyplayer.time() && lyrics.lines.get(counter + 1).startTimeMs > PublicValues.spotifyplayer.time()) {
                        if (!area.getText().equals(line.words)) {
                            area.setText(line.words + "\n" + lyrics.lines.get(counter + 1).words);
                        }
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    //End of song
                    area.setText(lyrics.lines.get(lyrics.lines.size() - 1).words);
                    break;
                }
                counter++;
            }
        }else{
            int counter = 0;
            for (ColoredLyricsLine line : new ArrayList<>(coloredLines)) {
                try {
                    if (line.startTimeMS < PublicValues.spotifyplayer.time() && coloredLines.get(counter + 1).startTimeMS > PublicValues.spotifyplayer.time()) {
                        break;
                    }else{
                        if(PublicValues.spotifyplayer.time() < coloredLines.get(0).getStartTimeMS()) continue;
                        coloredLines.remove(0);
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                counter++;
            }
            try {
                coloredLines.set(0, new ColoredLyricsLine(
                        PublicValues.globalFontColor.brighter().brighter(),
                        coloredLines.get(0).getTextContent(),
                        coloredLines.get(0).getStartTimeMS()
                ));
            }catch (IndexOutOfBoundsException ignored) {
            }
            repaint();
        }
    }

    private static class ColoredLyricsLine extends RAWTextArea.ColoredLine {
        private long startTimeMS;
        public ColoredLyricsLine(Color textColor, String textContent, long startTimeMS) {
            super(textColor, textContent);
            this.startTimeMS = startTimeMS;
        }

        public long getStartTimeMS() {
            return this.startTimeMS;
        }
    }

    public void append(String textContent, Color textColor, long startTimeMS) {
        coloredLines.add(new ColoredLyricsLine(textColor, textContent, startTimeMS));
        repaint();
    }

    private final ArrayList<ColoredLyricsLine> coloredLines = new ArrayList<>();

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int ycache = getFontMetrics(getFont()).getHeight() + 5;
        for (ColoredLine line : new ArrayList<>(coloredLines)) {
            g.setColor(PublicValues.globalFontColor);
            Rectangle2D r = g.getFontMetrics().getStringBounds(line.getTextContent(), g);
            g.drawString(line.getTextContent(), 5, ycache);
            ycache += getFontMetrics(getFont()).getHeight() + 5;
        }
        setPreferredSize(new Dimension(getWidth(), ycache + 10));
    }
}
