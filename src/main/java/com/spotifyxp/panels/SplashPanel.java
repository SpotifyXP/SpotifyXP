package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dpi.JComponentFactory;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.JImagePanel;
import com.spotifyxp.utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class SplashPanel {
    public static JFrame frame;
    public static JLabel linfo = new JLabel();
    public void show() {
        frame = new JFrame();
        JImagePanel image = new JImagePanel();
        linfo = new JLabel("Please wait...");
        image.setImage(new Resources().readToInputStream("spotifyxp.png"));
        frame.getContentPane().add(image);
        frame.setPreferredSize(new Dimension(290,300));
        try {
            frame.setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
        }catch (Exception e) {
            ConsoleLogging.Throwable(e);
            if(PublicValues.config.get(ConfigValues.hideExceptions.name).equals("false")) {
                ExceptionDialog.open(e);
            }
        }
        frame.add(linfo, BorderLayout.SOUTH);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
        frame.setLocation(frame.getLocation().x - frame.getWidth()/2, frame.getLocation().y - frame.getHeight()/2);
    }
    public static void hide() {
        frame.setVisible(false);
    }
}
