package com.spotifyxp.testing;



import com.spotifyxp.PublicValues;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.panels.ArtistPanel;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static JFrame frame = new JFrame("Test");
    public static void main(String[] args ) {
        PublicValues.language = new libLanguage();
        PublicValues.language.setAutoFindLanguage();
        PublicValues.language.setLanguageFolder("lang");
        ArtistPanel panel = new ArtistPanel();
        frame.setPreferredSize(new Dimension(800, 300));
        frame.setVisible(true);
        frame.pack();
    }

}
