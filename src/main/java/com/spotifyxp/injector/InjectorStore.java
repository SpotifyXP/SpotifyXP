package com.spotifyxp.injector;

import javax.swing.*;

public class InjectorStore {
    // (Extension Store)

    //Features:

    // Download and Install Extensions
    // Remove Extensions
    public InjectorStore() {
        open();
    }


    static class ContentPanel extends JPanel {
        public ContentPanel() {

        }
    }

    JFrame main;

    void open() {
        main = new JFrame("SpotifyXP - Extension Store");
    }
}
