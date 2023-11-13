package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.RAWTextArea;
import com.spotifyxp.utils.Resources;
import org.json.JSONException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static com.spotifyxp.panels.ContentPanel.playerarealyricsbutton;

public class LyricsDialog {
    final JFrame frame = new JFrame("SpotifyXP - Song Lyrics");
    UnofficialSpotifyAPI.Lyrics lyrics;
    final RAWTextArea area = new RAWTextArea();
    final JScrollPane pane = new JScrollPane(area);

    final LyricsMode mode = LyricsMode.LIVE;

    static class LyricsWord {
        public int column;
        public String word;
        public long ms;
    }

    final ArrayList<LyricsWord> words = new ArrayList<>();

    public enum LyricsMode {
        LIVE,
        SPOTIFY,
    }

    int oldw = 0;
    int oldh = 0;

    public boolean open(String uri) {
        words.clear();
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
                }catch (Exception e) {
                    ConsoleLogging.Throwable(e);
                    if(PublicValues.config.getString(ConfigValues.hideExceptions.name).equals("false")) {
                        ExceptionDialog.open(e);
                    }
                }
                frame.setPreferredSize(new Dimension(ContentPanel.frame.getWidth() / 2, ContentPanel.frame.getHeight() / 2));
                frame.setVisible(true);
                frame.pack();
            }

            int counter = 0;
            for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                LyricsWord lyricsWord = new LyricsWord();
                lyricsWord.word = line.words;
                lyricsWord.ms = line.startTimeMs;
                lyricsWord.column = counter;
                words.add(lyricsWord);
                area.append(line.words + "\n");
                counter++;
            }
            return true;
        }catch (JSONException e) {
            return false;
        }
    }

    public void close() {
        frame.setVisible(false);
    }

    public void setLyricsMode(LyricsMode lyricsMode) {
        //mode = lyricsMode;
    }

    int c = 0;

    public void triggerRefresh() {
        if(lyrics == null) {
            return;
        }
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
        c++;
    }
}
