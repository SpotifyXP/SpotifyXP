package com.spotifyxp.dpi;

import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class JComponentFactory {
    static int framew = 0;
    static int frameh = 0;
    public static ArrayList<JComponent> jcomponents = new ArrayList<JComponent>();

    public static JComponent createJComponent(JComponent tocreate) {
        jcomponents.add(tocreate);
        return tocreate;
    }

    public static void addJComponent(JComponent jcomponent) {
        jcomponents.add(jcomponent);
    }

    static int oldw;
    static int oldh;
    public static void enableResizing() {
        oldw = ContentPanel.frame.getWidth();
        oldh = ContentPanel.frame.getHeight();
        ContentPanel.frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int rw = ContentPanel.frame.getWidth() - oldw;
                int rh = ContentPanel.frame.getHeight() - oldh;
                for(JComponent component : jcomponents) {
                    if (rw > 0) {
                        // +
                        component.setSize(component.getWidth() + rw, component.getHeight());
                    }else{
                        // -
                        component.setSize(component.getWidth() - rw, component.getHeight());
                    }
                    if (rw > 0) {
                        // +
                        component.setSize(component.getWidth(), component.getHeight() + rh);
                    }else{
                        // -
                        component.setSize(component.getWidth(), component.getHeight() - rh);
                    }
                }
                oldw = ContentPanel.frame.getWidth();
                oldh = ContentPanel.frame.getHeight();
            }
        });
    }

    public static void applyDPI() {
        //Learning how to do this
    }
}
