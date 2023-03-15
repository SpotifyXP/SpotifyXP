package com.spotifyxp.engine.elements;

import com.spotifyxp.engine.Element;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.logging.ConsoleLogging;

import java.awt.*;
import java.awt.geom.Path2D;

public class Heart implements Element {
    int x = 0;
    int y = 0;
    String name = "Heart";
    int w = 0;
    int h = 0;
    Color bc = Color.white;
    Color lc = Color.black;
    boolean fill = false;
    EnginePanel ep = null;
    boolean d = false;

    public Heart(int width, int height, int xaxis, int yaxis, Color backgroundColor, Color lineColor) {
        w = width;
        h = height;
        x = xaxis;
        y = yaxis;
        bc = backgroundColor;
        lc = lineColor;
    }

    @Override
    public void setEnginePanelInstance(EnginePanel enginePanel) {
        ep = enginePanel;
    }

    @Override
    public void setDebug(boolean debug) {
        d = debug;
    }

    public void setFill(boolean fillit) {
        fill = fillit;
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
        float beX = x + w / 2;
        float beY = y + h;
        float c1DX = w  * 0.968f;
        float c1DY = h * 0.672f;
        float c2DX = w  * 0.281f;
        float c2DY = h * 1.295f;
        float teDY = h * 0.850f;
        Path2D.Float heartPath = new Path2D.Float();
        heartPath.moveTo(beX, beY);
        heartPath.curveTo(beX - c1DX, beY - c1DY, beX - c2DX, beY - c2DY, beX, beY - teDY);
        heartPath.curveTo(beX + c2DX, beY - c2DY, beX + c1DX, beY - c1DY, beX, beY);
        graphics2D.setColor(lc);
        if(fill) {
            graphics2D.fill(heartPath);
        }else {
            graphics2D.draw(heartPath);
        }
        //Reset colors
        graphics2D.setColor(Color.BLACK);
        if(d) {
            ConsoleLogging.changeName("DrawingEngine");
            ConsoleLogging.info("Drawing: '" + name + "' with x:" + x + " | y:" + y + " | width:" + w + " | height:" + h);
            ConsoleLogging.changeName("SpotifyXP");
        }
    }
}
