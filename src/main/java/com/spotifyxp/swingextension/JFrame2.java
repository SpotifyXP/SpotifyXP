package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;

import javax.swing.*;
import java.awt.*;

public class JFrame2 extends JFrame {
    public JFrame2(String title) {
        super.setTitle(title);
    }
    public JFrame2() {
    }

    public void close() {
        this.setVisible(false);
    }
    boolean aa = false;
    public void setAntiAliasingActive(boolean value) {
        aa = value;
    }
    @Override
    public void paintComponents(Graphics g) {
        if(aa) {
            Graphics2D g2d = (Graphics2D) g.create();
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2d.setRenderingHints(hints);
        }
        super.paintComponents(g);
    }

    public void open() {
        setJMenuBar(PublicValues.menuBar);
        this.setVisible(true);
        this.pack();
    }

    public void openMain() {
        this.setVisible(true);
        this.pack();
    }
}
