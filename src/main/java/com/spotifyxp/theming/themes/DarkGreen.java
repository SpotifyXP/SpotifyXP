package com.spotifyxp.theming.themes;

import com.formdev.flatlaf.FlatDarkLaf;
import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;

import javax.swing.*;
import java.awt.*;

public class DarkGreen implements Theme {
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
        PublicValues.borderColor = Color.gray;
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            ConsoleLogging.Throwable(e);
        }
        PublicValues.globalFontColor = Color.green;
    }
}
