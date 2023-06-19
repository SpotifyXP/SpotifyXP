package com.spotifyxp.engine.elements;

import com.spotifyxp.engine.Element;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.logging.ConsoleLogging;
import java.awt.*;
import java.awt.event.MouseEvent;

public class AddRemove implements Element {
    final int x;
    final int y;
    final String name = "AddRemove";
    final int w = 0;
    final int h = 0;
    boolean a;
    final Color lc;
    EnginePanel ep = null;
    boolean d = false;
    final int s;

    public AddRemove(int size, int xaxis, int yaxis, boolean add, Color lineColor) {
        s = size;
        x = xaxis;
        y = yaxis;
        a = add;
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

    @Override
    public void handleClick(int x, int y, MouseEvent e) {

    }

    public void setAdd(boolean add) {
        a = add;
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
        Circle.draw(s, x, y, lc, graphics2D);
        graphics2D.setColor(lc);
        if(a) {
            //Draw plus
            graphics2D.drawLine(x + s, y, x + s, y + s*2);
            graphics2D.drawLine(x, y + s, x + s*2, y + s);
        }else{
            //Draw minus
            graphics2D.drawLine(x, y + s, x + s*2, y + s);
        }
        if(d) {
            ConsoleLogging.changeName("DrawingEngine");
            ConsoleLogging.info("Drawing: '" + name + "' with x:" + x + " | y:" + y + " | width:" + w + " | height:" + h);
            ConsoleLogging.changeName("SpotifyXP");
        }
        graphics2D.setColor(Color.black);
    }
}
