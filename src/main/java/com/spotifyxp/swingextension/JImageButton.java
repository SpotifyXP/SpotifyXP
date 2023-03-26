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

public class JImageButton extends JButton {
    private BufferedImage image = null;
    public boolean isFilled = false;
    private String rad = "";

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
    public void setRotation(int percent) {
        rad = String.valueOf(((float) 360/100*percent) * 0.01745329252);
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
            Graphics2D graphics2D = (Graphics2D) g;
            if(!(rad.equals(""))) {
                graphics2D.rotate(Double.parseDouble(rad), (float)this.getWidth() / 2, (float)this.getHeight() / 2);
            }
            graphics2D.drawImage(image.getScaledInstance(this.getWidth() / 4,this.getHeight() /2, Image.SCALE_SMOOTH), this.getWidth()/3 + 3, this.getHeight()/4, null);
        }
    }
}
