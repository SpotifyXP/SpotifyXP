package com.spotifyxp.frames;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings({"CanBeFinal", "Convert2Lambda"})
public class SettingsFrame extends JPanel {
    private final JTextField mypalpathdisplay;
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

        mypalpathdisplay = new JTextField();
        mypalpathdisplay.setBounds(125, 50, 239, 20);
        mypal.add(mypalpathdisplay);
        mypalpathdisplay.setColumns(10);

        JButton mypalsetpathbutton = new JButton("Set Path"); //ToDo: Translate
        mypalsetpathbutton.setBounds(374, 50, 89, 23);
        mypal.add(mypalsetpathbutton);

        mypalsetpathbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JLabel mypalpathlabel = new JLabel("Mypal Path"); //ToDo: Translate
        mypalpathlabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mypalpathlabel.setBounds(23, 53, 70, 14);
        mypal.add(mypalpathlabel);

        JLabel mypalpathsettingslabel = new JLabel("Mypal path settings"); //ToDo: Translate
        mypalpathsettingslabel.setHorizontalAlignment(SwingConstants.CENTER);
        mypalpathsettingslabel.setBounds(125, 11, 239, 14);
        mypal.add(mypalpathsettingslabel);

        JPanel performance = new JPanel();
        performance.setBounds(0, 96, 484, 97);
        moduleHolder.add(performance);
        performance.setLayout(null);

        performanceoptionloadallrecommendations = new JToggleButton("Show all recommendations"); //ToDo: Translate
        performanceoptionloadallrecommendations.setBounds(24, 45, 208, 30);
        performance.add(performanceoptionloadallrecommendations);

        JLabel performanceoptionslabel = new JLabel("Performance options"); //ToDo: Translate
        performanceoptionslabel.setHorizontalAlignment(SwingConstants.CENTER);
        performanceoptionslabel.setBounds(143, 11, 208, 14);
        performance.add(performanceoptionslabel);

        performanceoptiondisableplayerstats = new JToggleButton("Disable player stats"); //ToDo: Translate
        performanceoptiondisableplayerstats.setBounds(254, 45, 208, 30);
        performance.add(performanceoptiondisableplayerstats);
        mypalpathdisplay.setEditable(false);
        mypalpathdisplay.setText(PublicValues.config.get("settings.mypal.path"));
        performanceoptionloadallrecommendations.setSelected(Boolean.getBoolean(PublicValues.config.get("settings.performance.showallrecommendations")));
        performanceoptiondisableplayerstats.setSelected(Boolean.getBoolean(PublicValues.config.get("settings.performance.displayplayerstats")));
        mypalsetpathbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileFilter filter = new FileNameExtensionFilter("EXE File","exe"); // ToDo: Translate
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Choose mypal executable"); // ToDo: Translate
                chooser.showOpenDialog(settingsframe);
                mypalpathdisplay.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
    }
    public void open() {
        //size 500x600
        JFrame frame = new JFrame("XPSpotify - Settings"); //ToDo: Translate
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
                PublicValues.config.write(ConfigValues.mypalpath.name, mypalpathdisplay.getText());
                //---
                frame.setVisible(false);
            }
        });
    }
}
