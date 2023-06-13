package com.spotifyxp.theming.themes;

import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;
import java.awt.*;

public class Light implements Theme {
    @Override
    public String getAuthor() {
        return "Werwolf2303";
    }

    @Override
    public boolean isLight() {
        return true;
    }

    @Override
    public void styleElement(Class component) {
        ConsoleLogging.error("Executing styleElement");
        System.out.println("Executing styleElement");
    }
}
