package com.spotifyxp.engine.elements;

import java.awt.*;

public class Circle {
    public static void draw(int radius, int x, int y, Color lineColor, Graphics2D graphics2D) {
        graphics2D.setColor(lineColor);
        graphics2D.drawOval(x, y, radius*2, radius*2);
        //Reset colors
        graphics2D.setColor(Color.black); //ToDo: Find out which color to reset to
        graphics2D.setBackground(Color.black); //ToDo: Find out which color to reset to
    }
    public static void fill(int radius, int x, int y, Color backgroundColor, Color lineColor, Graphics2D graphics2D) {

        graphics2D.setColor(lineColor);
        graphics2D.fillOval(x, y, radius*2, radius*2);
        //Reset colors
        graphics2D.setColor(Color.black); //ToDo: Find out which color to reset to
        graphics2D.setBackground(Color.black); //ToDo: Find out which color to reset to
    }
}
