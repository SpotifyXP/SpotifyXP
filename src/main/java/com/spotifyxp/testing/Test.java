package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.panels.SettingsPanel;
import com.spotifyxp.theming.themes.DarkGreen;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        PublicValues.fileslocation = new File("data").getAbsolutePath();
        PublicValues.configfilepath = PublicValues.fileslocation + "/config.properties";
        PublicValues.customSaveDir = true;
        PublicValues.appLocation = PublicValues.fileslocation;
        PublicValues.config = new Config();
        PublicValues.language = new libLanguage();
        PublicValues.language.setLanguageFolder("lang");
        PublicValues.theme = new DarkGreen();
        JFrame frame = new JFrame("Settings Test");
        frame.getContentPane().add(new SettingsPanel());
        frame.setPreferredSize(new Dimension(422, 506));
        frame.setVisible(true);
        frame.pack();
    }
}
