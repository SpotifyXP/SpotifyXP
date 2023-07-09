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

    public static void enableResizing() {
        //Learning how to do this
    }

    public static void applyDPI() {
        //Learning how to do this
    }
}
