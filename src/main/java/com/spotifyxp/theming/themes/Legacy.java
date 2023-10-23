package com.spotifyxp.theming.themes;

import com.spotifyxp.PublicValues;
import com.spotifyxp.injector.InjectingPoints;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.theming.Theme;

import javax.swing.*;
import java.awt.*;

public class Legacy implements Theme {
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
        PublicValues.borderColor = Color.gray;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (RuntimeException | UnsupportedLookAndFeelException | ClassNotFoundException |
                 InstantiationException | IllegalAccessException e) {
            ConsoleLogging.Throwable(e);
        }
        InjectingPoints.registerOnFrameReady(new Runnable() {
            @Override
            public void run() {
                ContentPanel.legacyswitch.setBackgroundAt(0, Color.white);
                ContentPanel.legacyswitch.setBackgroundAt(1, Color.white);
                ContentPanel.legacyswitch.setBackgroundAt(2, Color.white);
                ContentPanel.legacyswitch.setBackgroundAt(3, Color.white);
                ContentPanel.legacyswitch.setBackgroundAt(4, Color.white);
                ContentPanel.legacyswitch.setBackgroundAt(5, Color.white);
                ContentPanel.legacyswitch.setBackgroundAt(6, Color.white);
            }
        });
    }
}
