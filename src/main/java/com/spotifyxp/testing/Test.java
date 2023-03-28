package com.spotifyxp.testing;



import com.spotifyxp.panels.ArtistPanel;
import com.spotifyxp.swingextension.JImageButton;
import com.spotifyxp.utils.Resources;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static void main(String[] args ) {
        JFrame frame = new JFrame("Test");
        ArtistPanel panel = new ArtistPanel();
        frame.add(panel.getPanel());
        frame.setPreferredSize(new Dimension(800, 300));
        frame.setVisible(true);
        frame.pack();
    }

}
