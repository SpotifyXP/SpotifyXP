package com.spotifyxp.swingextension;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class JFrame extends javax.swing.JFrame {
    public JFrame(String title) {
        super.setTitle(title);
    }

    public JFrame() {
    }

    public Point getCenter() {
        return new Point(getLocation().x / 2, getLocation().y / 2);
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
        if (aa) {
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2d.setRenderingHints(hints);
        }
        super.paintComponents(g);
    }

    public void open() {
        try {
            setIconImage(ImageIO.read(new Resources().readToInputStream("spotifyxp.png")));
        } catch (IOException e) {
            GraphicalMessage.openException(e);
            ConsoleLogging.Throwable(e);
        }
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void pack() {
        if (ContentPanel.frame.isVisible()) {
            Utils.moveToScreen(this, PublicValues.screenNumber);
        }
        super.pack();
    }
}
