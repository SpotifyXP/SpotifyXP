package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.dummy.DummyJFrame;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.engine.elements.AddRemove;
import com.spotifyxp.engine.elements.Heart;
import com.spotifyxp.engine.elements.Image;
import com.spotifyxp.events.LoggerEvent;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.utils.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static void main(String[] args ) {
        PublicValues.language = new libLanguage();
        PublicValues.language.setAutoFindLanguage();
        PublicValues.language.setLanguageFolder("lang");
        HTMLDialog dialog = new HTMLDialog(new LoggerEvent() {
            @Override
            public void log(String s) {

            }

            @Override
            public void err(String s) {

            }

            @Override
            public void info(String s) {

            }

            @Override
            public void crit(String s) {

            }
        });
        dialog.getDialog().setPreferredSize(new Dimension(400, 500));
        try {
            String out = new Resources().readToString("about.html");
            StringBuilder cache = new StringBuilder();
            for(String s : out.split("\n")) {
                if(s.contains("(TRANSLATE)")) {
                    s = s.replace(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", ""), PublicValues.language.translate(s.split("\\(TRANSLATE\\)")[1].replace("(TRANSLATE)", "")));
                    s = s.replace("(TRANSLATE)", "");
                }
                cache.append(s);
            }
            dialog.open(new DummyJFrame(), PublicValues.language.translate("ui.menu.help.about"), cache.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dialog.getDialog().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.getDialog().dispose();
            }
        });
    }

    public void old() {
        JFrame frame = new JFrame("Test Engine");
        ConsoleLogging logging = new ConsoleLogging("SpotifyXP");
        logging.setColored(true);
        logging.setShowTime(false);
        EnginePanel panel = new EnginePanel();
        panel.setBackground(Color.black);
        Heart heart = new Heart(60, 60, 30, 30, Color.white, Color.white);
        Image image = new Image(50, 50, 120, 120);
        AddRemove addRemove = new AddRemove(30, 30, 240, true, Color.white);
        panel.setDebug(true);
        panel.addElement(image);
        panel.addElement(addRemove);
        panel.addElement(heart);
        image.setImage(new Resources().readToInputStream("spotifyxp.png"));
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setBackground(Color.black);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }
}
