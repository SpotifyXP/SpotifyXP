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

    public static void debug(Object o) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showMessageDialog(null, o.toString(), "Debug", JOptionPane.OK_OPTION);
    }

    public static void sorryError(String additional) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("critical.sorry.text") + " Additional Info => " + additional, PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
    }

    public static void sorryErrorExit() {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("critical.sorry.text"), PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
        System.exit(2);
    }

    public static void sorryErrorExit(String additional) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("critical.sorry.text") + " Additional Info => " + additional, PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
        System.exit(2);
    }


    public static boolean stuck() {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        return JOptionPane.showConfirmDialog(null, PublicValues.language.translate("message.stuck.text"), PublicValues.language.translate("message.stuck.title"), JOptionPane.YES_NO_OPTION) == 0;
    }
}
