package com.spotifyxp.testing;


import com.spotifyxp.PublicValues;
import com.spotifyxp.api.UnofficialSpotifyAPI;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.deps.com.spotify.metadata.Metadata;
import com.spotifyxp.panels.HomePanel;
import com.spotifyxp.swingextension.JFrame2;
import com.spotifyxp.utils.PlayerUtils;
import com.spotifyxp.utils.Token;

import javax.swing.*;
import java.awt.*;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("TestFrame");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(new HomePanel(800, 600).getComponent());
        frame.setVisible(true);
        frame.pack();
    }
}
