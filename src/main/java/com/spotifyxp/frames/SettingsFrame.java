package com.spotifyxp.frames;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings({"CanBeFinal", "Convert2Lambda"})
public class SettingsFrame extends JPanel {
    private final JToggleButton performanceoptionloadallrecommendations;
    private final JToggleButton performanceoptiondisableplayerstats;
    private JFrame settingsframe;
    public SettingsFrame() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel moduleHolder = new JPanel();
        add(moduleHolder, BorderLayout.CENTER);
        moduleHolder.setLayout(null);

        JPanel mypal = new JPanel();
        mypal.setBounds(0, 0, 484, 97);
        moduleHolder.add(mypal);
        mypal.setLayout(null);

        JPanel performance = new JPanel();
        performance.setBounds(0, 96, 484, 97);
        moduleHolder.add(performance);
        performance.setLayout(null);

        performanceoptionloadallrecommendations = new JToggleButton(PublicValues.language.translate("ui.settings.performance.showall"));
        performanceoptionloadallrecommendations.setBounds(24, 45, 208, 30);
        performance.add(performanceoptionloadallrecommendations);

        JLabel performanceoptionslabel = new JLabel(PublicValues.language.translate("ui.settings.performance.label"));
        performanceoptionslabel.setHorizontalAlignment(SwingConstants.CENTER);
        performanceoptionslabel.setBounds(143, 11, 208, 14);
        performance.add(performanceoptionslabel);

        performanceoptiondisableplayerstats = new JToggleButton(PublicValues.language.translate("ui.settings.performance.disableplayerstats"));
        performanceoptiondisableplayerstats.setBounds(254, 45, 208, 30);
        performance.add(performanceoptiondisableplayerstats);
        performanceoptionloadallrecommendations.setSelected(Boolean.getBoolean(PublicValues.config.get("settings.performance.showallrecommendations")));
        performanceoptiondisableplayerstats.setSelected(Boolean.getBoolean(PublicValues.config.get("settings.performance.displayplayerstats")));
    }
    public void open() {
        //size 500x600
        JFrame frame = new JFrame(PublicValues.language.translate("ui.settings.title"));
        settingsframe = frame;
        frame.getContentPane().add(this);
        frame.setPreferredSize(new Dimension(500, 600));
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Apply settings
                PublicValues.config.write(ConfigValues.disableplayerstats.name, String.valueOf(performanceoptiondisableplayerstats.isSelected()));
                PublicValues.config.write(ConfigValues.showallrecommendations.name, String.valueOf(performanceoptionloadallrecommendations.isSelected()));
                //---
                frame.setVisible(false);
            }
        });
    }
}
