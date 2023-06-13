package com.spotifyxp.theming.themes;

import com.spotifyxp.theming.Theme;
import java.awt.*;

public class Dark implements Theme {
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
        System.out.println("Execute styleElement");
    }
}
