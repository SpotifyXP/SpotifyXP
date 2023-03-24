package com.spotifyxp.engine.elements;

import com.spotifyxp.engine.Element;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.logging.ConsoleLogging;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Image implements Element {
    int w;
    int h;
    int x;
    int y;
    EnginePanel ep;
    final String name = "Image";
    boolean d = false;
    private BufferedImage image = null;
    public Image(int width, int height, int xaxis, int yaxis) {
        w = width;
        h = height;
        x = xaxis;
        y = yaxis;
    }
    public void setImage(String filename) {
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ep.triggerRepaint();
    }
    public void setImage(File file) {
        try {
            image = ImageIO.read(file);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        ep.triggerRepaint();
    }
    public void setImage(URL url) {
        try {
            image = ImageIO.read(url);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        ep.triggerRepaint();
    }
    public void setImage(InputStream inputStream) {
        try {
            image = ImageIO.read(inputStream);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        ep.triggerRepaint();
    }
    public void setImage(ImageInputStream imageInputStream) {
        try {
            image = ImageIO.read(imageInputStream);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        ep.triggerRepaint();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        if(image!=null) {
            graphics2D.drawImage(image.getScaledInstance(w,h, java.awt.Image.SCALE_REPLICATE), x, y, null);
            if(d) {
                ConsoleLogging.changeName("DrawingEngine");
                ConsoleLogging.info("Drawing: '" + name + "' with x:" + x + " | y:" + y + " | width:" + w + " | height:" + h);
                ConsoleLogging.changeName("SpotifyXP");
            }
        }
    }

    @Override
    public void setEnginePanelInstance(EnginePanel enginePanel) {
        ep = enginePanel;
    }

    @Override
    public void setDebug(boolean debug) {
        d = debug;
    }

    @Override
    public void handleClick(int x, int y, MouseEvent e) {

    }
}
