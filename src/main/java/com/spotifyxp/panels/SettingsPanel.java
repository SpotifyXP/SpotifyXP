package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.dpi.JComponentFactory;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.Utils;
import org.apache.xmlgraphics.io.Resource;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SettingsPanel extends JPanel {
    final SettingsPanel panel = this;
    public static JTextField settingsbrowserpath;
    public static JButton settingspathsetbutton;
    public static JRadioButton settingsuidisableplayerstats;
    public static JComboBox settingsuiselecttheme;
    public static JComboBox settingsplaybackselectquality;
    public static JButton settingsplaybackopenequalizerbutton;
    public static JRadioButton settingsdisableexceptions;
    public static JLabel settingslanguagelabel;
    public static JLabel settingslanguageselectlabel;
    public static JComboBox settingslanguageselect;

    public SettingsPanel() {
        setBounds(100, 100, 800, 600);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        JLabel settingsbrowserlabel =(JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.settings.browser.label")));
        settingsbrowserlabel.setBounds(10, 11, 206, 29);
        add(settingsbrowserlabel);

        settingsbrowserlabel.setForeground(PublicValues.globalFontColor);

        settingsbrowserpath = (JTextField) JComponentFactory.createJComponent(new JTextField());
        settingsbrowserpath.setBounds(81, 54, 364, 20);
        add(settingsbrowserpath);
        settingsbrowserpath.setColumns(10);

        JLabel settingsbrowserpathlable = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.settings.mypal.path.label")));
        settingsbrowserpathlable.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsbrowserpathlable.setBounds(10, 57, 46, 14);
        add(settingsbrowserpathlable);

        settingsbrowserpathlable.setForeground(PublicValues.globalFontColor);

        settingspathsetbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.settings.mypal.path.choosebutton")));
        settingspathsetbutton.setBounds(478, 53, 89, 23);
        add(settingspathsetbutton);

        settingspathsetbutton.setForeground(PublicValues.globalFontColor);

        JLabel settingsuilabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.settings.ui.label")));
        settingsuilabel.setBounds(10, 122, 89, 14);
        add(settingsuilabel);

        settingsuilabel.setForeground(PublicValues.globalFontColor);

        settingsdisableexceptions = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("general.exception.hide")));
        settingsdisableexceptions.setBounds(160, 155, 140, 23);
        add(settingsdisableexceptions);

        settingsdisableexceptions.setForeground(PublicValues.globalFontColor);

        settingsuidisableplayerstats = (JRadioButton) JComponentFactory.createJComponent(new JRadioButton(PublicValues.language.translate("ui.settings.performance.disableplayerstats")));
        settingsuidisableplayerstats.setBounds(28, 155, 199, 23);
        add(settingsuidisableplayerstats);

        settingsuidisableplayerstats.setForeground(PublicValues.globalFontColor);

        settingsuiselecttheme = (JComboBox) JComponentFactory.createJComponent(new JComboBox());
        for(Theme theme : ThemeLoader.availableThemes) {
            ((DefaultComboBoxModel)settingsuiselecttheme.getModel()).addElement(Utils.getClassName(theme.getClass()) + " from " + theme.getAuthor());
        }
        settingsuiselecttheme.setBounds(442, 155, 230, 22);
        add(settingsuiselecttheme);

        JLabel settingsuithemelabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.settings.theme")));
        settingsuithemelabel.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsuithemelabel.setBounds(287, 159, 120, 14);
        add(settingsuithemelabel);

        settingsuithemelabel.setForeground(PublicValues.globalFontColor);

        JLabel settingsplaybacklabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.settings.playback.label")));
        settingsplaybacklabel.setBounds(10, 224, 71, 14);
        add(settingsplaybacklabel);

        settingsplaybacklabel.setForeground(PublicValues.globalFontColor);

        settingsplaybackselectquality = (JComboBox) JComponentFactory.createJComponent(new JComboBox(new String[] {
                "Normal", "High", "Very_High"
        }));
        settingsplaybackselectquality.setBounds(178, 255, 206, 22);
        add(settingsplaybackselectquality);

        JLabel settingsplaybackselectqualitylabel = (JLabel) JComponentFactory.createJComponent(new JLabel(PublicValues.language.translate("ui.settings.quality")));
        settingsplaybackselectqualitylabel.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsplaybackselectqualitylabel.setBounds(28, 259, 120, 14);
        add(settingsplaybackselectqualitylabel);

        settingsplaybackselectqualitylabel.setForeground(PublicValues.globalFontColor);

        settingsplaybackopenequalizerbutton = (JButton) JComponentFactory.createJComponent(new JButton(PublicValues.language.translate("ui.settings.uninstall")));
        settingsplaybackopenequalizerbutton.setBounds(478, 255, 146, 23);
        add(settingsplaybackopenequalizerbutton);

        settingsplaybackopenequalizerbutton.setForeground(PublicValues.globalFontColor);

        settingsplaybackopenequalizerbutton.addActionListener(e -> triggerUninstall());

        settingspathsetbutton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter filter = new FileNameExtensionFilter(PublicValues.language.translate("ui.settings.mypal.path.filter"),"exe");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle(PublicValues.language.translate("ui.settings.mypal.path.choose"));
            chooser.showOpenDialog(panel);
            settingsbrowserpath.setText(chooser.getSelectedFile().getAbsolutePath());
        });

        settingslanguagelabel = new JLabel(PublicValues.language.translate("ui.settings.language"));
        settingslanguagelabel.setBounds(28, 320, 140, 20);
        add(settingslanguagelabel);

        settingslanguagelabel.setForeground(PublicValues.globalFontColor);

        settingslanguageselect = new JComboBox();
        settingslanguageselect.setBounds(162, 352, 199, 27);
        add(settingslanguageselect);

        settingslanguageselectlabel = new JLabel(PublicValues.language.translate("ui.settings.langselect"));
        settingslanguageselectlabel.setBounds(28, 356, 140, 16);
        add(settingslanguageselectlabel);

        settingslanguageselectlabel.setForeground(PublicValues.globalFontColor);

        ArrayList<String> selectcache = new ArrayList<>();

        for(Locale locale : Locale.getAvailableLocales()) {
            if(locale.getDisplayLanguage().equals("")) {
                continue;
            }
            if(selectcache.contains(locale.getDisplayLanguage())) {
                continue;
            }
            if(new Resources(true).readToInputStream("lang/" + locale.getLanguage() + ".json") != null) {
                selectcache.add(locale.getDisplayLanguage());
            }
        }

        for(String s : selectcache) {
            ((DefaultComboBoxModel) settingslanguageselect.getModel()).addElement(s);
        }

        settingslanguageselect.getModel().setSelectedItem(PublicValues.config.get(ConfigValues.language.name));
        settingsdisableexceptions.setSelected(Boolean.parseBoolean(PublicValues.config.get(ConfigValues.hideExceptions.name)));
        settingsuidisableplayerstats.setSelected(Boolean.parseBoolean(PublicValues.config.get(ConfigValues.disableplayerstats.name)));
        settingsbrowserpath.setText(PublicValues.config.get(ConfigValues.mypalpath.name));
        settingsuiselecttheme.getModel().setSelectedItem(PublicValues.config.get(ConfigValues.theme.name));
        settingsplaybackselectquality.getModel().setSelectedItem(PublicValues.config.get(ConfigValues.audioquality.name));

        if(settingslanguageselect.getModel().getSelectedItem().toString().equals(ConfigValues.language.name)) {
            settingslanguageselect.getModel().setSelectedItem(PublicValues.language.translate("ui.settings.nolang"));
        }
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
        PublicValues.config.write(ConfigValues.language.name, settingslanguageselect.getModel().getSelectedItem().toString());
        PublicValues.config.write(ConfigValues.hideExceptions.name, String.valueOf(settingsdisableexceptions.isSelected()));
        PublicValues.config.write(ConfigValues.theme.name, settingsuiselecttheme.getModel().getSelectedItem().toString().split(" from ")[0]);
        PublicValues.config.write(ConfigValues.mypalpath.name, settingsbrowserpath.getText());
        PublicValues.config.write(ConfigValues.disableplayerstats.name, String.valueOf(settingsuidisableplayerstats.isSelected()));
        JOptionPane.showConfirmDialog(null, PublicValues.language.translate("ui.settings.pleaserestart"), PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
    }

    public static void triggerUninstall() {
        ProcessBuilder builder;
        try {
            Files.copy(new Resources().readToInputStream("JavaSetupTool.jar"), Paths.get(PublicValues.tempPath + File.separator + "SpotifyXP-Uninstaller.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(PublicValues.appLocation + File.separator + "uninstaller.xml"), Paths.get(PublicValues.tempPath + File.separator + "uninstaller.xml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            builder = new ProcessBuilder("cmd.exe", "/c", "\"" + System.getProperty("java.home") + "/bin/java\"" + " -jar " + PublicValues.tempPath + "/SpotifyXP-Uninstaller.jar");
        }else{
            builder = new ProcessBuilder("bash", "-c", "\"" + System.getProperty("java.home") + "/bin/java\"" + " -jar " + PublicValues.tempPath + "/SpotifyXP-Uninstaller.jar");
        }
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
