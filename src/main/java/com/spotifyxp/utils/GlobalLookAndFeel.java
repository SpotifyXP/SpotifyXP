package com.spotifyxp.utils;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class GlobalLookAndFeel {
    public void setLookAndFeel(String classname) {
        if(classname.contains("flatlaf")) {
            try {
                switch (classname) {
                    case "com.formdev.flatlaf.Darcula": {
                        UIManager.setLookAndFeel(new FlatDarculaLaf());
                    }
                    case "com.formdev.flatlaf.Dark": {
                        UIManager.setLookAndFeel(new FlatDarkLaf());
                    }
                    case "com.formdev.flatlaf.IntelliJ": {
                        UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    }
                    case "com.formdev.flatlaf.Light": {
                        UIManager.setLookAndFeel(new FlatLightLaf());
                    }
                    case "com.formdev.flatlaf.MacDark": {
                        UIManager.setLookAndFeel(new FlatMacDarkLaf());
                    }
                    case "com.formdev.flatlaf.MacLight": {
                        UIManager.setLookAndFeel(new FlatMacLightLaf());
                    }
                }
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }else {
            try {
                UIManager.setLookAndFeel(classname);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                     IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
