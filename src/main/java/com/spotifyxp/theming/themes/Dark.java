package com.spotifyxp.theming.themes;

import com.formdev.flatlaf.FlatDarkLaf;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;

import javax.swing.*;
import java.awt.*;

public class Dark implements Theme {
    @Override
    public String getAuthor() {
        return "Werwolf2303";
    }

    @Override
    public boolean isLight() {
        return false;
    }

    @Override
    public void initTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
