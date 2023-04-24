package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.Resources;
import org.json.JSONException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class LyricsDialog {
    JFrame frame = new JFrame("SpotifyXP - Song Lyrics");
    UnofficialSpotifyAPI.Lyrics lyrics;

    JTextArea area = new JTextArea();

    LyricsMode mode = LyricsMode.LIVE;

    static class LyricsWord {
        public int column;
        public String word;
        public long ms;
    }

    ArrayList<LyricsWord> words = new ArrayList<>();

    public enum LyricsMode {
        LIVE,
        SPOTIFY,
    }

    public void open(String uri) {
        words.clear();
        area.setText("");
        try {
            if (frame.isVisible()) {
                lyrics = new UnofficialSpotifyAPI(ContentPanel.api.getSpotifyApi().getAccessToken()).getLyrics(uri);
            } else {
                lyrics = new UnofficialSpotifyAPI(ContentPanel.api.getSpotifyApi().getAccessToken()).getLyrics(uri);
                area.setEditable(false);
                frame.add(area, BorderLayout.CENTER);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        if (PublicValues.theme == Theme.LEGACY || PublicValues.theme == Theme.WINDOWS || PublicValues.theme == Theme.MacOSLight || PublicValues.theme == Theme.QuaQua || PublicValues.theme == Theme.UGLY) {
                            ContentPanel.playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonedark.svg"));
                        } else {
                            ContentPanel.playerarealyricsbutton.setImage(new Resources().readToInputStream("icons/microphonewhite.svg"));
                        }
                        ContentPanel.playerarealyricsbutton.isFilled = false;
                    }
                });
                frame.setPreferredSize(new Dimension(ContentPanel.frame.getWidth(), ContentPanel.frame.getHeight()));
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
        }catch (JSONException e) {
            //No lyrics available
        }
    }

    public void close() {
        frame.setVisible(false);
    }

    void removeLast() {
        try {
            area.setText(area.getText(1, area.getText().length()));
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLyricsMode(LyricsMode lyricsMode) {
        //mode = lyricsMode; ToDo: Implement Spotify Mode
    }

    int c = 0;

    public void triggerRefresh() {
        if(lyrics == null) {
            return;
        }
        if(mode!=LyricsMode.SPOTIFY) {
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
            //Spotify Mode
            int counter = 0;
            for (UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
                try {
                    if (line.startTimeMs < PublicValues.spotifyplayer.time() && lyrics.lines.get(counter + 1).startTimeMs > PublicValues.spotifyplayer.time()) {
                        //Found current playing word
                        removeLast();
                    }
                } catch (IndexOutOfBoundsException e) {
                    //End of song
                    //area.setText(lyrics.lines.get(lyrics.lines.size() - 1).words);
                    break;
                }
                counter++;
            }
        }
        c++;
    }
}
