package com.spotifyxp.panels;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import ch.randelshofer.quaqua.jaguar.Quaqua15JaguarLookAndFeel;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.theming.ThemeEngine;
import com.spotifyxp.theming.includedThemes.Luna;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SettingsPanel extends JPanel {
    final SettingsPanel panel = this;
    public static JTextField settingsbrowserpath;
    public static JButton settingspathsetbutton;
    public static JRadioButton settingsuidisableplayerstats;
    public static JComboBox settingsuiselecttheme;
    public static JComboBox settingsplaybackselectquality;
    public static JButton settingsplaybackopenequalizerbutton;
    ArrayList<Theme> themecache = new ArrayList<>();

    public SettingsPanel() {
        setBounds(100, 100, 800, 600);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        JLabel settingsbrowserlabel = new JLabel(PublicValues.language.translate("ui.settings.browser.label"));
        settingsbrowserlabel.setBounds(10, 11, 206, 29);
        add(settingsbrowserlabel);

        settingsbrowserpath = new JTextField();
        settingsbrowserpath.setBounds(81, 54, 364, 20);
        add(settingsbrowserpath);
        settingsbrowserpath.setColumns(10);

        JLabel settingsbrowserpathlable = new JLabel(PublicValues.language.translate("ui.settings.mypal.path.label"));
        settingsbrowserpathlable.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsbrowserpathlable.setBounds(10, 57, 46, 14);
        add(settingsbrowserpathlable);

        settingspathsetbutton = new JButton(PublicValues.language.translate("ui.settings.mypal.path.choosebutton"));
        settingspathsetbutton.setBounds(478, 53, 89, 23);
        add(settingspathsetbutton);

        JLabel settingsuilabel = new JLabel(PublicValues.language.translate("ui.settings.ui.label"));
        settingsuilabel.setBounds(10, 122, 89, 14);
        add(settingsuilabel);

        settingsuidisableplayerstats = new JRadioButton(PublicValues.language.translate("ui.settings.performance.disableplayerstats"));
        settingsuidisableplayerstats.setBounds(28, 155, 199, 23);
        add(settingsuidisableplayerstats);

        settingsuiselecttheme = new JComboBox();
        //for(Theme theme : ThemeEngine.themes) {
        //    themecache.add(theme);
        //    ((DefaultListModel) settingsuiselecttheme.getModel()).addElement(theme.name);
        //}
        for(Enum e : com.spotifyxp.designs.Theme.values()) {
            ((DefaultComboBoxModel) settingsuiselecttheme.getModel()).addElement(((com.spotifyxp.designs.Theme)e).getAsString());
        }
        settingsuiselecttheme.setBounds(442, 155, 230, 22);
        add(settingsuiselecttheme);

        JLabel settingsuithemelabel = new JLabel(PublicValues.language.translate("ui.settings.theme"));
        settingsuithemelabel.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsuithemelabel.setBounds(287, 159, 120, 14);
        add(settingsuithemelabel);

        JLabel settingsplaybacklabel = new JLabel(PublicValues.language.translate("ui.settings.playback.label"));
        settingsplaybacklabel.setBounds(10, 224, 71, 14);
        add(settingsplaybacklabel);

        settingsplaybackselectquality = new JComboBox(new String[] {
                "Normal", "High", "Very_High"
        });
        settingsplaybackselectquality.setBounds(178, 255, 206, 22);
        add(settingsplaybackselectquality);

        JLabel settingsplaybackselectqualitylabel = new JLabel(PublicValues.language.translate("ui.settings.quality"));
        settingsplaybackselectqualitylabel.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsplaybackselectqualitylabel.setBounds(28, 259, 120, 14);
        add(settingsplaybackselectqualitylabel);

        settingsplaybackopenequalizerbutton = new JButton(PublicValues.language.translate("ui.settings.equalizer.open"));
        settingsplaybackopenequalizerbutton.setBounds(478, 255, 146, 23);
        add(settingsplaybackopenequalizerbutton);

        settingsplaybackopenequalizerbutton.addActionListener(e -> JOptionPane.showConfirmDialog(null, "Equalizer not implemented yet", "ToDo", JOptionPane.OK_CANCEL_OPTION));

        settingspathsetbutton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter filter = new FileNameExtensionFilter(PublicValues.language.translate("ui.settings.mypal.path.filter"),"exe");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle(PublicValues.language.translate("ui.settings.mypal.path.choose"));
            chooser.showOpenDialog(panel);
            settingsbrowserpath.setText(chooser.getSelectedFile().getAbsolutePath());
        });

        settingsuidisableplayerstats.setSelected(Boolean.parseBoolean(PublicValues.config.get(ConfigValues.disableplayerstats.name)));
        settingsbrowserpath.setText(PublicValues.config.get(ConfigValues.mypalpath.name));
        settingsuiselecttheme.getModel().setSelectedItem(PublicValues.config.get(ConfigValues.theme.name));
        settingsplaybackselectquality.getModel().setSelectedItem(PublicValues.config.get(ConfigValues.audioquality.name));
    }

    public static void applySettings() {
        switch ((String)settingsplaybackselectquality.getModel().getSelectedItem()) {
            case "Normal":
                PublicValues.config.write(ConfigValues.audioquality.name, "NORMAL");
                break;
            case "High":
                PublicValues.config.write(ConfigValues.audioquality.name, "HIGHT");
                break;
            case "Very_High":
                PublicValues.config.write(ConfigValues.audioquality.name, "VERY_HIGH");
                break;
        }
        PublicValues.config.write(ConfigValues.theme.name, settingsuiselecttheme.getModel().getSelectedItem().toString().toUpperCase());
        PublicValues.config.write(ConfigValues.mypalpath.name, settingsbrowserpath.getText());
        PublicValues.config.write(ConfigValues.disableplayerstats.name, String.valueOf(settingsuidisableplayerstats.isSelected()));
        JOptionPane.showConfirmDialog(null, "Please restart SpotifyXP to apply the changes", "Info", JOptionPane.OK_CANCEL_OPTION);
    }
}
