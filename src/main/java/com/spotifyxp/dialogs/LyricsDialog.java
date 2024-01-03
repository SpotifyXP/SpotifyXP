package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.swingextension.RAWTextArea;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import org.json.JSONException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.spotifyxp.panels.PlayerArea.playerarealyricsbutton;

public class LyricsDialog {
    final JFrame2 frame = new JFrame2("SpotifyXP - Song Lyrics");
    UnofficialSpotifyAPI.Lyrics lyrics;
    final RAWTextArea area = new RAWTextArea();
    final JScrollPane pane = new JScrollPane(area);

    int oldw = 0;
    int oldh = 0;

    public boolean open(String uri) {
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
        }catch (JSONException e) {
            return false;
        }
    }

    public void close() {
        frame.setVisible(false);
    }

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
    }
}
