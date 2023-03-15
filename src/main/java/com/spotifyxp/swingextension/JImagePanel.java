package com.spotifyxp.swingextension;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JImagePanel extends JPanel {
    private BufferedImage image = null;

    void refresh() {
        this.repaint();
    }

    public void setImage(String filename) {
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        refresh();
    }
    public void setImage(File file) {
        try {
            image = ImageIO.read(file);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        refresh();
    }
    public void setImage(URL url) {
        try {
            image = ImageIO.read(url);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        refresh();
    }
    public void setImage(InputStream inputStream) {
        try {
            image = ImageIO.read(inputStream);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        refresh();
    }
    public void setImage(ImageInputStream imageInputStream) {
        try {
            image = ImageIO.read(imageInputStream);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        refresh();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image!=null) {
            g.drawImage(image.getScaledInstance(this.getWidth(),this.getHeight(), Image.SCALE_REPLICATE), 0, 0, null);
        }
    }
}
