package com.spotifyxp.theming.themes;

import com.formdev.flatlaf.FlatLightLaf;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;

import javax.swing.*;
import java.awt.*;

public class Light implements Theme {
    @Override
    public String getAuthor() {
        return "Werwolf2303 [NO UPDATES]";
    }

    @Override
    public boolean isLight() {
        return true;
    }

    @Override
    public void initTheme() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
