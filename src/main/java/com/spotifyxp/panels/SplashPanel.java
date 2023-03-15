package com.spotifyxp.panels;

import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.Resources;

import javax.swing.*;
import java.awt.*;

public class SplashPanel {
    private static JFrame frame;
    public void show() {
        frame = new JFrame();
        JImagePanel image = new JImagePanel();
        JLabel l = new JLabel("Please wait...");
        image.setImage(new Resources().readToInputStream("spotifyxp.png"));
        frame.getContentPane().add(image);
        frame.setPreferredSize(new Dimension(290,300));
        frame.add(l, BorderLayout.SOUTH);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
        frame.setLocation(frame.getLocation().x - frame.getWidth()/2, frame.getLocation().y - frame.getHeight()/2);
    }
    public static void hide() {
        frame.setVisible(false);
    }
}
