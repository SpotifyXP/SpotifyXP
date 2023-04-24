package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.SplashPanel;

import javax.swing.*;

public class GraphicalMessage {
    public static void bug(String where) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.graphicalmessage.bug") + where, PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
    }
    public static void sorryError() {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("critical.sorry.text"), PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
    }
}
