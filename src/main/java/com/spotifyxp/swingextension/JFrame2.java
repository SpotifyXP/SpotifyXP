package com.spotifyxp.swingextension;

import javax.swing.*;
import java.awt.*;

public class JFrame2 extends JFrame {
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
        this.setVisible(true);
        this.pack();
    }
}
