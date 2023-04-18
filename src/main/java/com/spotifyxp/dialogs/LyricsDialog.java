package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import java.awt.*;

public class LyricsDialog {
    JFrame frame = new JFrame("SpotifyXP - Song Lyrics");
    UnofficialSpotifyAPI.Lyrics lyrics;

    JTextArea area = new JTextArea();

    public void open(String uri) {
        if(frame.isVisible()) {
            lyrics = new UnofficialSpotifyAPI(ContentPanel.api.getSpotifyApi().getAccessToken()).getLyrics(uri);
        }else {
            lyrics = new UnofficialSpotifyAPI(ContentPanel.api.getSpotifyApi().getAccessToken()).getLyrics(uri);
            frame.add(area, BorderLayout.CENTER);
            frame.setPreferredSize(new Dimension(ContentPanel.frame.getWidth(), ContentPanel.frame.getHeight()));
            frame.setVisible(true);
            frame.pack();
        }
    }

    public void triggerRefresh() {
        if(lyrics == null) {
            return;
        }
        int counter = 0;
        for(UnofficialSpotifyAPI.LyricsLine line : lyrics.lines) {
            try {
                if (line.startTimeMs < PublicValues.spotifyplayer.time() && lyrics.lines.get(counter + 1).startTimeMs > PublicValues.spotifyplayer.time()) {
                    if (!area.getText().equals(line.words)) {
                        area.setText(line.words + "\n" + lyrics.lines.get(counter + 1).words);
                    }
                }
            }catch (IndexOutOfBoundsException e) {
                //End of song
                area.setText(lyrics.lines.get(lyrics.lines.size()-1).words);
                break;
            }
            counter++;
        }
    }
}
