package com.spotifyxp.engine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EnginePanel extends JPanel {
    ArrayList<Element> elements = new ArrayList<>();
    boolean d = false;
    boolean f = true;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(f) {
            f = false;
            return;
        }
        for(Element e : elements) {
            e.drawElement((Graphics2D) g);
        }
    }

    public void setDebug(boolean debug) {
        d = debug;
    }

    public void triggerRepaint() {
        repaint();
    }

    public void addElement(Element e) {
        e.setEnginePanelInstance(this);
        e.setDebug(d);
        elements.add(e);
    }
}
