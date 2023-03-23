package com.spotifyxp.testing;

import com.spotifyxp.PublicValues;
import com.spotifyxp.analytics.Analytics;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.designs.Theme;
import com.spotifyxp.dialogs.HTMLDialog;
import com.spotifyxp.dummy.DummyJFrame;
import com.spotifyxp.engine.EnginePanel;
import com.spotifyxp.engine.elements.AddRemove;
import com.spotifyxp.engine.elements.Heart;
import com.spotifyxp.engine.elements.Image;
import com.spotifyxp.engine.non2d.Volume;
import com.spotifyxp.events.LoggerEvent;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.swingextension.ContextMenu;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.Resources;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@SuppressWarnings("EmptyMethod")
public class Test {
    public static void main(String[] args ) throws IOException {
        PublicValues.theme = Theme.DARK;
        JFrame frame = new JFrame("Test");
        JPanel contentPanel = new JPanel();
        EnginePanel panel = new EnginePanel();
        Volume volume = new Volume();
        volume.setVertical();
        panel.addNon2DElement(volume);
        contentPanel.add(volume);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.add(contentPanel);
        frame.setVisible(true);
        frame.pack();
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
