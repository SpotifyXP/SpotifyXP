package com.spotifyxp.theming.themes;

import com.formdev.flatlaf.FlatDarkLaf;
import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
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
        ContentPanel.frame.setBackground(Color.getColor("#3c3f41"));
        ContentPanel.legacyswitch.setBackground(new Color(63, 63, 63));
        PublicValues.borderColor = Color.black;
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            ConsoleLogging.Throwable(e);
        }
        PublicValues.globalFontColor = Color.green;
    }
}
