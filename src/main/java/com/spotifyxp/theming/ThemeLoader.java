package com.spotifyxp.theming;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.themes.DarkGreen;
import com.spotifyxp.theming.themes.Legacy;
import com.spotifyxp.utils.Utils;

import java.util.ArrayList;

public class ThemeLoader {
    public static String extraSearchPath = ""; //Injector entry point
    public static Theme loadedTheme = null;
    static String loadedThemePath = "";
    public static final ArrayList<Theme> availableThemes = new ArrayList<>(); //Extensions can add their themes here

    public ThemeLoader() {
        availableThemes.add(new DarkGreen());
        availableThemes.add(new Legacy());
    }

    public void tryLoadTheme(String name) throws UnknownThemeException {
        boolean found = false;
        for(Theme theme : availableThemes) {
            if(Utils.getClassName(theme.getClass()).toLowerCase().contains(name.toLowerCase())) {
                executeTheme(theme);
                PublicValues.theme = theme;
                found = true;
            }
        }
        if(!found) {
            throw new UnknownThemeException(name);
        }
    }

    public void loadTheme(String name) throws UnknownThemeException {
        boolean found = false;
        for(Theme theme : availableThemes) {
            if(Utils.getClassName(theme.getClass()).equals(name)) {
                executeTheme(theme);
                PublicValues.theme = theme;
                found = true;
            }
        }
        if(!found) {
            throw new UnknownThemeException(name);
        }
    }

    void executeTheme(Theme theme) {
        theme.initTheme();
        ConsoleLogging.info("Loaded Theme => " + Utils.getClassName(theme.getClass()) + " from => " + theme.getAuthor());
    }

    public static class UnknownThemeException extends Exception {
        public UnknownThemeException(String themeName) {
            super(themeName);
        }
    }
}
