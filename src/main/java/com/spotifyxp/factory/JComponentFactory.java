package com.spotifyxp.factory;

import javax.swing.*;
import java.util.ArrayList;

public class JComponentFactory {
    private static final ArrayList<JComponent> components = new ArrayList<>();

    public static JComponent createJComponent(JComponent tocreate) {
        components.add(tocreate);
        return tocreate;
    }

    public static void addJComponent(JComponent jComponent) {
        components.add(jComponent);
    }

    public static JComponent getComponentAt(int number) {
        return components.get(number);
    }

    public static int getComponentsNumber() {
        return components.size();
    }
}
