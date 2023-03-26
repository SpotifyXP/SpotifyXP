package com.spotifyxp.engine.elements;

import com.spotifyxp.engine.EnginePanel;

import java.awt.*;

public class Circle {
    public static void draw(int radius, int x, int y, Color lineColor, Graphics2D graphics2D) {
        graphics2D.setColor(lineColor);
        graphics2D.drawOval(x, y, radius*2, radius*2);
    }
    public static void fill(int radius, int x, int y, Color backgroundColor, Color lineColor, Graphics2D graphics2D) {

        graphics2D.setColor(lineColor);
        graphics2D.fillOval(x, y, radius*2, radius*2);
    }
}
