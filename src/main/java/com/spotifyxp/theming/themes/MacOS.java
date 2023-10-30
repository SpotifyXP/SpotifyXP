package com.spotifyxp.theming.themes;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;

import javax.swing.*;

public class MacOS implements Theme {
    public MacOS() {
    }

    public String getAuthor() {
        return "Werwolf2303";
    }

    public boolean isLight() {
        return true;
    }

    public void initTheme() {
        try {
            UIManager.setLookAndFeel(new QuaquaLookAndFeel());
        } catch (UnsupportedLookAndFeelException var2) {
            ConsoleLogging.Throwable(var2);
        }

    }
}
