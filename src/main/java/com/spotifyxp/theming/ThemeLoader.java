package com.spotifyxp.theming;

import com.spotifyxp.PublicValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.themes.*;
import com.spotifyxp.utils.Utils;

import java.util.ArrayList;

public class ThemeLoader {
    public static Theme loadedTheme = null;
    static String loadedThemePath = "";
    private static final ArrayList<Theme> availableThemes = new ArrayList<>();

    public ThemeLoader() {
        availableThemes.add(new DarkGreen());
        availableThemes.add(new Legacy());
        if(!PublicValues.isMacOS) {
            //Unsupported byte count: 16
            availableThemes.add(new MacOS());
        }
        availableThemes.add(new Ugly());
        availableThemes.add(new CustomTheme());
    }

    public static boolean hasTheme(String name) {
        for(Theme t : availableThemes) {
            if(Utils.getClassName(t.getClass()).equals(name)) {
                return true;
            }
        }
        return false;
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

    public static ArrayList<Theme> getAvailableThemes() {
        return availableThemes;
    }

    public static void addTheme(Theme theme) {
        availableThemes.add(theme);
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
