package com.spotifyxp.dpi;

import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class JComponentFactory {
    static int framew = 0;
    static int frameh = 0;
    public static ArrayList<JComponent> jcomponents = new ArrayList<JComponent>();
    public static ArrayList<HashMap<ResizeRule, JComponent>> jcompodnentswr = new ArrayList<>();


    // ---- By ChatGPT ----
    public static enum ResizeRule {
        X,
        Y,
        W,
        H,
        XY,
        XW,
        XH,
        YW,
        YH,
        WH,
        XYW,
        XYH,
        XWH,
        YWH,
        XYWH,
    }
    //----------------------

    /**
     * Creates the given JComponent and adds that to a list of components
     * @param tocreate instance of a JComponent
     * @return instance of JComponent
     */
    public static JComponent createJComponent(JComponent tocreate) {
        jcomponents.add(tocreate);
        return tocreate;
    }

    public static JComponent createJComponent(JComponent tocreate, ResizeRule resizeRule) {
        return null;
    }

    /**
     * Adds the given JComponent to alist
     * @param jcomponent instance of JComponent
     */
    public static void addJComponent(JComponent jcomponent) {
        jcomponents.add(jcomponent);
    }

    static int oldw;
    static int oldh;

    /**
     * Very broken resizing feature
     */
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
                        component.setBounds(component.getX() + rw, component.getY(), component.getWidth() + rw, component.getHeight());
                    }else{
                        // -
                        component.setBounds(component.getX() - rw, component.getY(), component.getWidth() - rw, component.getHeight());
                    }

                    if (rh > 0) {
                        // +
                        component.setBounds(component.getX(), component.getY() + rh, component.getWidth(), component.getHeight() + rh);
                    }else{
                        // -
                        component.setBounds(component.getX(), component.getY() - rh, component.getWidth(), component.getHeight() - rh);
                    }
                }
                oldw = ContentPanel.frame.getWidth();
                oldh = ContentPanel.frame.getHeight();
            }
        });
    }

    /**
     * Still learning how to do this
     */
    public static void applyDPI() {
        //Learning how to do this
    }
}
