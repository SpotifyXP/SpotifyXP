package com.spotifyxp.theming.themes;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;

import javax.swing.*;

public class MacOS implements Theme {
    @Override
    public String getAuthor() {
        return "Werwolf2303";
    }

    @Override
    public boolean isLight() {
        return true;
    }

    @Override
    public void initTheme() {
        try {
            UIManager.setLookAndFeel(new QuaquaLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            ConsoleLogging.Throwable(e);
        }
    }
}
