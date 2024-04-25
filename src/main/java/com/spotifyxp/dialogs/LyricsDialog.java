package com.spotifyxp.dialogs;

import ch.randelshofer.quaqua.border.OverlayBorder;
import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.manager.InstanceManager;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.PaintPanel;
import com.spotifyxp.swingextension.RAWTextArea;
import com.spotifyxp.utils.ClipboardUtil;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import org.json.JSONException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static com.spotifyxp.panels.PlayerArea.playerarealyricsbutton;

public class LyricsDialog extends JDialog {
    private JPanel contentPanel;
    private JScrollPane pane;
    private JPanel paintPanel;
    private final ArrayList<ColoredLyricsLine> displayedLines = new ArrayList<>();

    public LyricsDialog() {
        setTitle("SpotifyXP - Song Lyrics"); //ToDo: Translate
        setContentPane(contentPanel);
    }

    UnofficialSpotifyAPI.Lyrics lyrics;
    String uri;

    public boolean open(String uri) {
        try {
            if (isVisible()) {
                lyrics = new UnofficialSpotifyAPI(InstanceManager.getSpotifyApi().getAccessToken()).getLyrics(uri);
                //New lyrics
            } else {
                lyrics = new UnofficialSpotifyAPI(InstanceManager.getSpotifyApi().getAccessToken()).getLyrics(uri);
                addWindowListener(new WindowAdapter() {
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
                    setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
                } catch (Exception e) {
                    ConsoleLogging.Throwable(e);
                    if (PublicValues.config.getString(ConfigValues.hideExceptions.name).equals("false")) {
                        GraphicalMessage.openException(e);
                    }
                }
                ContextMenu menu = new ContextMenu(paintPanel);
                menu.addItem(PublicValues.language.translate("ui.general.copy"), new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder buffer = new StringBuilder();
                        for(ColoredLyricsLine lines : new ArrayList<>(displayedLines)) {
                            buffer.append(lines.getTextContent()).append("\n");
                        }
                        ClipboardUtil.set(buffer.toString());
                    }
                });
                setPreferredSize(new Dimension(ContentPanel.frame.getWidth() / 2, ContentPanel.frame.getHeight() / 2));
                pack();
                setVisible(true);
            }
            displayedLines.clear();
            coloredLines.clear();
            for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                append(line.words, PublicValues.globalFontColor, line.startTimeMs);
            }
            triggerRefresh();
            Events.subscribe(SpotifyXPEvents.playerSeekedBackwards.getName(), seekedBackwards());
            Events.subscribe(SpotifyXPEvents.playerSeekedForwards.getName(), seekedForwards());
            this.uri = uri;
            return true;
        }catch (JSONException e) {
            return false;
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
                lyrics = new UnofficialSpotifyAPI(InstanceManager.getSpotifyApi().getAccessToken()).getLyrics(uri);
                coloredLines.clear();
                for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                    coloredLines.add(new LyricsDialog.ColoredLyricsLine(PublicValues.globalFontColor, line.words, line.startTimeMs));
                }
                int activeLine = 0;
                for (LyricsDialog.ColoredLyricsLine line : new ArrayList<>(coloredLines)) {
                    if (line.startTimeMS < InstanceManager.getPlayer().getPlayer().time()) {
                        activeLine++;
                        break;
                    }
                }
                try {
                    coloredLines.set(activeLine, new LyricsDialog.ColoredLyricsLine(
                            PublicValues.globalFontColor.brighter().brighter(),
                            coloredLines.get(activeLine).getTextContent(),
                            coloredLines.get(activeLine).getStartTimeMS()
                    ));
                } catch (IndexOutOfBoundsException ignored) {
                }
                repaint();
            }
        };
    }

    private void createUIComponents() {
        paintPanel = new PaintPanel(this::paintLines);
    }

    private static class ColoredLyricsLine extends RAWTextArea.ColoredLine {
        private final long startTimeMS;

        public ColoredLyricsLine(Color textColor, String textContent, long startTimeMS) {
            super(textColor, textContent);
            this.startTimeMS = startTimeMS;
        }

        public long getStartTimeMS() {
            return this.startTimeMS;
        }
    }

    public void append(String textContent, Color textColor, long startTimeMS) {
        coloredLines.add(new LyricsDialog.ColoredLyricsLine(textColor, textContent, startTimeMS));
        repaint();
    }

    private final ArrayList<LyricsDialog.ColoredLyricsLine> coloredLines = new ArrayList<>();

    public void close() {
        Events.unsubscribe(SpotifyXPEvents.playerSeekedBackwards.getName(), seekedBackwards());
        Events.unsubscribe(SpotifyXPEvents.playerSeekedForwards.getName(), seekedForwards());
        setVisible(false);
    }

    public void triggerRefresh() {
        if (lyrics == null) {
            return;
        }
        int counter = 0;
        displayedLines.clear();
        for (LyricsDialog.ColoredLyricsLine line : new ArrayList<>(coloredLines)) {
            try {
                if (line.startTimeMS < PublicValues.spotifyplayer.time() && coloredLines.get(counter + 1).startTimeMS > PublicValues.spotifyplayer.time()) {
                    displayedLines.add(line);
                    try {
                        displayedLines.add(coloredLines.get(counter + 1));
                    }catch (IndexOutOfBoundsException ignored) {
                    }
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                displayedLines.add(line);
                break;
            }
            counter++;
        }
        try {
            displayedLines.set(0, new LyricsDialog.ColoredLyricsLine(
                    PublicValues.globalFontColor.brighter().brighter(),
                    displayedLines.get(0).getTextContent(),
                    displayedLines.get(0).getStartTimeMS()
            ));
        }catch (IndexOutOfBoundsException ignored) {
        }
        repaint();
    }

    public void paintLines(Graphics g) {
        try {
            int ycache = getFontMetrics(getFont()).getHeight() + 5;
            for (RAWTextArea.ColoredLine line : new ArrayList<>(displayedLines)) {
                g.setColor(line.getTextColor());
                g.drawString(line.getTextContent(), 5, ycache);
                ycache += getFontMetrics(getFont()).getHeight() + 5;
            }
            setPreferredSize(new Dimension(getWidth(), ycache + 10));
        }catch (NullPointerException ignored) {
        }
    }
}
