package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.panels.ContentPanel;
import com.spotifyxp.panels.SplashPanel;

import javax.swing.*;

public class GraphicalMessage {
    public static void bug(String where) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("ui.graphicalmessage.bug") + where, PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
    }
    public static void sorryError() {
        if (SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("critical.sorry.text"), PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
    }

    public static void pleaseRestart() {
        if (SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("ui.settings.pleaserestart"), PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
    }

    public static void debug(Object o) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showMessageDialog(ContentPanel.frame, o.toString(), "Debug", JOptionPane.OK_OPTION);
    }

    public static void sorryError(String additional) {
        if (SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("critical.sorry.text") + " Additional Info => " + additional, PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
    }

    public static void sorryErrorExit() {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        int selection = JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("critical.sorry.text"), PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
        if(selection == JOptionPane.CANCEL_OPTION) {
            openException(new UnknownError());
            return;
        }
        System.exit(2);
    }

    public static void sorryErrorExit(String additional) {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        int selection = JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("critical.sorry.text") + " Additional Info => " + additional, PublicValues.language.translate("critical.sorry.title"), JOptionPane.OK_CANCEL_OPTION);
        if(selection == JOptionPane.CANCEL_OPTION) {
            openException(new Throwable(additional));
            return;
        }
        System.exit(2);
    }


    public static boolean stuck() {
        if(SplashPanel.frame.isVisible()) {
            SplashPanel.frame.setAlwaysOnTop(false);
        }
        return JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("message.stuck.text"), PublicValues.language.translate("message.stuck.title"), JOptionPane.YES_NO_OPTION) == 0;
    }

    /**
     * Adds an exception to the list (add to the exception counter)
     * @param ex instance of an Exception
     */
    public static void openException(Throwable ex) {
        if(ContentPanel.errorQueue != null) {
            ContentPanel.errorQueue.add(new ExceptionDialog(ex));
        }
    }
}
