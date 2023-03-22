package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SettingsPanel extends JPanel {
    SettingsPanel panel = this;
    public static JTextField settingsbrowserpath;
    public static JButton settingspathsetbutton;
    public static JRadioButton settingsuidisableplayerstats;
    public static JComboBox settingsuiselecttheme;
    public static JComboBox settingsplaybackselectquality;
    public static JButton settingsplaybackopenequalizerbutton;

    public SettingsPanel() {
        setBounds(100, 100, 800, 600);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        JLabel settingsbrowserlabel = new JLabel("Browser Settings"); //ToDo: Translate
        settingsbrowserlabel.setBounds(10, 11, 206, 29);
        add(settingsbrowserlabel);

        settingsbrowserpath = new JTextField();
        settingsbrowserpath.setBounds(81, 54, 364, 20);
        add(settingsbrowserpath);
        settingsbrowserpath.setColumns(10);

        JLabel settingsbrowserpathlable = new JLabel("Path"); //ToDo: Translate
        settingsbrowserpathlable.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsbrowserpathlable.setBounds(10, 57, 46, 14);
        add(settingsbrowserpathlable);

        settingspathsetbutton = new JButton("Set Path"); //ToDo: Translate
        settingspathsetbutton.setBounds(478, 53, 89, 23);
        add(settingspathsetbutton);

        JLabel settingsuilabel = new JLabel("UI"); //ToDo: Translate
        settingsuilabel.setBounds(10, 122, 89, 14);
        add(settingsuilabel);

        settingsuidisableplayerstats = new JRadioButton("Disable player stats"); //ToDo: Translate
        settingsuidisableplayerstats.setBounds(28, 155, 199, 23);
        add(settingsuidisableplayerstats);

        settingsuiselecttheme = new JComboBox(new String[] {
                "Light", "Dark"
        }); //ToDo: Translate
        settingsuiselecttheme.setBounds(442, 155, 230, 22);
        add(settingsuiselecttheme);

        JLabel settingsuithemelabel = new JLabel("Select Theme"); //ToDo: Translate
        settingsuithemelabel.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsuithemelabel.setBounds(287, 159, 120, 14);
        add(settingsuithemelabel);

        JLabel settingsplaybacklabel = new JLabel("Playback"); //ToDo: Translate
        settingsplaybacklabel.setBounds(10, 224, 71, 14);
        add(settingsplaybacklabel);

        settingsplaybackselectquality = new JComboBox(new String[] {
                "Normal", "High", "Very_High"
        }); //ToDo: Translate
        settingsplaybackselectquality.setBounds(178, 255, 206, 22);
        add(settingsplaybackselectquality);

        JLabel settingsplaybackselectqualitylabel = new JLabel("Select Quality"); //ToDo: Translate
        settingsplaybackselectqualitylabel.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsplaybackselectqualitylabel.setBounds(28, 259, 120, 14);
        add(settingsplaybackselectqualitylabel);

        settingsplaybackopenequalizerbutton = new JButton("Open Equalizer"); //ToDo: Translate
        settingsplaybackopenequalizerbutton.setBounds(478, 255, 146, 23);
        add(settingsplaybackopenequalizerbutton);

        settingsplaybackopenequalizerbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(null, "Equalizer not implemented yet", "ToDo", JOptionPane.OK_CANCEL_OPTION);
            }
        });

        settingspathsetbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileFilter filter = new FileNameExtensionFilter(PublicValues.language.translate("ui.settings.mypal.path.filter"),"exe");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle(PublicValues.language.translate("ui.settings.mypal.path.choose"));
                chooser.showOpenDialog(panel);
                settingsbrowserpath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
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
        switch ((String)settingsuiselecttheme.getModel().getSelectedItem()) {
            case "Light":
                PublicValues.config.write(ConfigValues.theme.name, "LIGHT");
                break;
            case "Dark":
                PublicValues.config.write(ConfigValues.theme.name, "DARK");
                break;
        }
        PublicValues.config.write(ConfigValues.mypalpath.name, settingsbrowserpath.getText());
        PublicValues.config.write(ConfigValues.disableplayerstats.name, String.valueOf(settingsuidisableplayerstats.isSelected()));
    }
}
