package com.spotifyxp.utils;

import javax.swing.*;

public class GraphicalMessage {
    public static void bug(String where) {
        JOptionPane.showConfirmDialog(null, "A bug was encountered please report this! Where: " + where, "Info", JOptionPane.OK_CANCEL_OPTION); //ToDo: Translate
    }
}
