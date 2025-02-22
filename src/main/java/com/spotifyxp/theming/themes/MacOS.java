package com.spotifyxp.theming.themes;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.utils.Utils;

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

        if(Utils.getJavaVersion() > 8) {
            ConsoleLogging.error("Quaqua doesn't support Java version 9 or higher");
            return;
        }

        try {
            UIManager.setLookAndFeel(new QuaquaLookAndFeel());
        } catch (UnsupportedLookAndFeelException var2) {
            ConsoleLogging.Throwable(var2);
        }

    }
}
