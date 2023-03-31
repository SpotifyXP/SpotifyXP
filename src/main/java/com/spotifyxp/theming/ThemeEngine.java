package com.spotifyxp.theming;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.Resources;
import org.json.JSONObject;

import java.util.ArrayList;

public class ThemeEngine {
    public static ArrayList<Theme> themes = new ArrayList<>();
    public ThemeEngine() {
        ConsoleLogging.debug("Initializing Theme Engine"); //ToDo: Translate
        initThemes();
        ConsoleLogging.debug("Initializing of Theme Engine is done"); //ToDo: Translate
    }

    void initThemes() {
        //Adding default themes
        Theme theme = new Theme();
        theme.name = "Light";
        theme.classPath = "com.formdev.flatlaf.FlatLightLaf";
        theme.isLookAndFeel = true;
        themes.add(theme);
        theme.name = "Dark";
        theme.classPath = "com.formdev.flatlaf.FlatDarkLaf";
        themes.add(theme);
        theme.name = "Luna";
        theme.classPath = "com.spotifyxp.theming.includedThemes.Luna";
        theme.isLookAndFeel = false;
        //---
    }

    void applyTheme() {
        boolean foundTheme = false;
        for(Theme theme : themes) {
            if(theme.name.equals(PublicValues.config.get(ConfigValues.theme.name))) {
                foundTheme = true;
            }
        }
        if(!foundTheme) {
            //PublicValues.theme = themes.get(1); //Defaulting to dark theme
        }
    }
}
