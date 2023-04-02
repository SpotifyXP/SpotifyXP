package com.spotifyxp.utils;

import com.spotifyxp.PublicValues;

import javax.swing.*;

public class GraphicalMessage {
    public static void bug(String where) {
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.graphicalmessage.bug") + where, PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
    }
}
