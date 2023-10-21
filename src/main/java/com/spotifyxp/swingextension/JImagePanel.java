package com.spotifyxp.swingextension;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JImagePanel extends JPanel {
    private BufferedImage image = null;
    private byte[] imagebytes;
    public boolean isFilled = false;
    private String rad = "";

    void refresh() {
        try {
            image = ImageIO.read(new ByteArrayInputStream(imagebytes));
        } catch (IOException ex) {
            ConsoleLogging.Throwable(ex);
        }
        this.repaint();
    }

    public void setImage(String filename) {
        try {
            imagebytes = Files.readAllBytes(Paths.get(filename));
        } catch (IOException ex) {
            ConsoleLogging.Throwable(ex);
        }
        refresh();
    }
    public void setImage(File file) {
        try {
            imagebytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        }catch (IOException ex) {
            ConsoleLogging.Throwable(ex);
        }
        refresh();
    }
    public void setRotation(int percent) {
        rad = String.valueOf(((float) 360/100*percent) * 0.01745329252);
        refresh();
    }
    public void setImage(URL url) {
        try {
            imagebytes = IOUtils.toByteArray(url);
        }catch (IOException ex) {
            ConsoleLogging.Throwable(ex);
        }
        refresh();
    }
    public void setImage(InputStream inputStream) {
        try {
            imagebytes = IOUtils.toByteArray(inputStream);
        }catch (IOException ex) {
            ConsoleLogging.Throwable(ex);
            ExceptionDialog.open(ex);
        }
        refresh();
    }
    public void setImage(ImageInputStream imageInputStream) {
        try {
            imageInputStream.readFully(imagebytes);
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
            ExceptionDialog.open(e);
        }
        refresh();
    }

    public InputStream getImageStream() {
        if(imagebytes == null || imagebytes.length == 0) {
            return null;
        }
        return new ByteArrayInputStream(imagebytes);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image == null) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) g;
        if(!(rad.isEmpty())) {
            graphics2D.rotate(Double.parseDouble(rad), (float)this.getWidth() / 2, (float)this.getHeight() / 2);
        }
        graphics2D.drawImage(image.getScaledInstance(this.getWidth(),this.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
    }
}
