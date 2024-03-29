package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.factory.Factory;
import com.spotifyxp.lastfm.LFMValues;
import com.spotifyxp.lastfm.LastFMLogin;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.JScrollText;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
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

@SuppressWarnings({"unchecked", "rawtypes", "resource"})
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
    public static JLabel settingsbrowserlabel;
    public static JLabel settingsbrowserpathlable;
    public static JLabel settingsuilabel;
    public static JLabel settingsuithemelabel;
    public static JLabel settingsplaybacklabel;
    public static JLabel settingsplaybackselectqualitylabel;
    public static JScrollText settingslastfmloginlabel;
    public static JButton settingslastfmlogout;
    public static JButton settingslastfmlogin;
    public static JRadioButton settingsturnoffspotifyconnect;

    //Borders
    public static JPanel browsersettingsborder;
    public static JPanel settingsuiborder;
    public static JPanel playbackborder;
    public static JPanel lastfmborder;
    //

    public SettingsPanel() {
        setBounds(100, 100, 422, 506);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        // Initiate and add borders
        browsersettingsborder = new JPanel();
        browsersettingsborder.setBorder(new TitledBorder(new LineBorder(new Color(171, 171, 171), 1, true), PublicValues.language.translate("ui.settings.browser.label")));
        browsersettingsborder.setBounds(20, 6, 382, 104);
        browsersettingsborder.setLayout(null);
        add(browsersettingsborder);
        settingsuiborder = new JPanel();
        settingsuiborder.setBorder(new TitledBorder(new LineBorder(new Color(171, 171, 171), 1, true), PublicValues.language.translate("ui.settings.ui.label")));
        settingsuiborder.setBounds(19, 122, 382, 178);
        settingsuiborder.setLayout(null);
        add(settingsuiborder);
        playbackborder = new JPanel();
        playbackborder.setBorder(new TitledBorder(new LineBorder(new Color(171, 171, 171), 1, true), PublicValues.language.translate("ui.settings.playback.label")));
        playbackborder.setBounds(20, 312, 382, 83);
        playbackborder.setLayout(null);
        add(playbackborder);
        lastfmborder = new JPanel();
        lastfmborder.setLayout(null);
        lastfmborder.setBorder(new TitledBorder(new LineBorder(new Color(171, 171, 171), 1, true), PublicValues.language.translate("ui.lastfm.settings.border")));
        lastfmborder.setBounds(20, 405, 382, 60);
        add(lastfmborder);
        //

        settingslastfmloginlabel = new JScrollText(PublicValues.language.translate("ui.lastfm.settings.loggedinas").replace("%s", LFMValues.username + "  "));
        settingslastfmloginlabel.setBounds(10, 25, 120, 20);
        lastfmborder.add(settingslastfmloginlabel);

        settingslastfmlogin = new JButton(PublicValues.language.translate("ui.login"));
        settingslastfmlogin.setBounds(190, 25, 85, 20);
        lastfmborder.add(settingslastfmlogin);

        settingslastfmlogin.addActionListener(e -> new LastFMLogin().open(() -> {
            if(!PublicValues.config.getString(ConfigValues.lastfmusername.name).isEmpty()) settingslastfmlogout.setEnabled(true);
            if(!PublicValues.config.getString(ConfigValues.lastfmusername.name).isEmpty()) settingslastfmlogin.setEnabled(false);
            if(!PublicValues.config.getString(ConfigValues.lastfmusername.name).isEmpty()) settingslastfmloginlabel.setText(PublicValues.language.translate("ui.lastfm.settings.loggedinas").replace("%s", PublicValues.config.getString(ConfigValues.lastfmusername.name) + "  "));
        }));

        settingslastfmlogout = new JButton(PublicValues.language.translate("ui.logout"));
        settingslastfmlogout.setBounds(285, 25, 85, 20);
        lastfmborder.add(settingslastfmlogout);

        settingslastfmlogout.setEnabled(false);

        settingslastfmlogout.addActionListener(e -> {
            PublicValues.config.write(ConfigValues.lastfmusername.name, "");
            PublicValues.config.write(ConfigValues.lastfmpassword.name, "");
            settingslastfmlogout.setEnabled(false);
            settingslastfmlogin.setEnabled(true);
            settingslastfmloginlabel.setText(PublicValues.language.translate("ui.lastfm.settings.loggedinas").replace("%s", PublicValues.config.getString(ConfigValues.lastfmusername.name) + "  "));
        });

        settingsbrowserlabel = new JLabel(PublicValues.language.translate("ui.settings.browser.label"));
        settingsbrowserlabel.setBounds(10, 451, 206, 29);
        //add(settingsbrowserlabel);

        settingsbrowserlabel.setForeground(PublicValues.globalFontColor);

        settingsbrowserpath = new JTextField();
        settingsbrowserpath.setBounds(6, 39, 370, 26);
        browsersettingsborder.add(settingsbrowserpath);
        settingsbrowserpath.setColumns(10);

        settingsbrowserpathlable = new JLabel(PublicValues.language.translate("ui.settings.mypal.path.label"));
        settingsbrowserpathlable.setBounds(17, 17, 348, 16);
        browsersettingsborder.add(settingsbrowserpathlable);
        settingsbrowserpathlable.setHorizontalAlignment(SwingConstants.LEFT);

        settingsbrowserpathlable.setForeground(PublicValues.globalFontColor);

        settingspathsetbutton = new JButton(PublicValues.language.translate("ui.settings.mypal.path.choosebutton"));
        settingspathsetbutton.setBounds(100, 69, 175, 29);
        browsersettingsborder.add(settingspathsetbutton);

        settingspathsetbutton.setForeground(PublicValues.globalFontColor);

        settingsuilabel = new JLabel(PublicValues.language.translate("ui.settings.ui.label"));
        settingsuilabel.setBounds(10, 435, 290, 14);
        //add(settingsuilabel);

        settingsuilabel.setForeground(PublicValues.globalFontColor);

        settingsdisableexceptions = new JRadioButton(PublicValues.language.translate("general.exception.hide"));
        settingsdisableexceptions.setBounds(6, 18, 370, 23);
        settingsuiborder.add(settingsdisableexceptions);

        settingsdisableexceptions.setForeground(PublicValues.globalFontColor);

        settingsuidisableplayerstats = new JRadioButton(PublicValues.language.translate("ui.settings.performance.disableplayerstats"));
        settingsuidisableplayerstats.setBounds(6, 53, 370, 23);
        settingsuiborder.add(settingsuidisableplayerstats);

        settingsuidisableplayerstats.setForeground(PublicValues.globalFontColor);

        settingsuiselecttheme = new JComboBox();
        for(Theme theme : ThemeLoader.getAvailableThemes()) {
            ((DefaultComboBoxModel)settingsuiselecttheme.getModel()).addElement(Utils.getClassName(theme.getClass()) + " from " + theme.getAuthor());
        }
        settingsuiselecttheme.setBounds(159, 85, 217, 30);
        settingsuiborder.add(settingsuiselecttheme);

        settingsuithemelabel = new JLabel(PublicValues.language.translate("ui.settings.theme"));
        settingsuithemelabel.setBounds(6, 90, 151, 16);
        settingsuiborder.add(settingsuithemelabel);
        settingsuithemelabel.setHorizontalAlignment(SwingConstants.RIGHT);

        settingsuithemelabel.setForeground(PublicValues.globalFontColor);

        settingsplaybacklabel = new JLabel(PublicValues.language.translate("ui.settings.playback.label"));
        settingsplaybacklabel.setBounds(10, 448, 269, 14);
        //add(settingsplaybacklabel);

        settingsplaybacklabel.setForeground(PublicValues.globalFontColor);

        settingsplaybackselectquality = new JComboBox(new String[] {
                "Normal", "High", "Very_High"
        });
        settingsplaybackselectquality.setBounds(167, 28, 206, 22);
        playbackborder.add(settingsplaybackselectquality);

        settingsplaybackselectqualitylabel = new JLabel(PublicValues.language.translate("ui.settings.quality"));
        settingsplaybackselectqualitylabel.setBounds(6, 30, 149, 14);
        playbackborder.add(settingsplaybackselectqualitylabel);
        settingsplaybackselectqualitylabel.setHorizontalAlignment(SwingConstants.RIGHT);

        settingsturnoffspotifyconnect = new JRadioButton(PublicValues.language.translate("ui.settings.spconnect"));
        settingsturnoffspotifyconnect.setForeground(PublicValues.globalFontColor);

        settingsturnoffspotifyconnect.setBounds(120, 55, 200, 20);

        playbackborder.add(settingsturnoffspotifyconnect);


        settingsplaybackselectquality.setForeground(PublicValues.globalFontColor);

        settingsplaybackopenequalizerbutton = new JButton(PublicValues.language.translate("ui.settings.uninstall"));
        settingsplaybackopenequalizerbutton.setBounds(205, 445, 197, 23);
        //add(settingsplaybackopenequalizerbutton);

        settingsplaybackopenequalizerbutton.setEnabled(false);

        settingsplaybackopenequalizerbutton.setForeground(PublicValues.globalFontColor);

        settingsplaybackopenequalizerbutton.addActionListener(e -> triggerUninstall());

        settingspathsetbutton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter filter = new FileNameExtensionFilter(PublicValues.language.translate("ui.settings.mypal.path.filter"),"exe");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle(PublicValues.language.translate("ui.settings.mypal.path.choose"));
            chooser.showOpenDialog(panel);
            if(chooser.getSelectedFile() != null) settingsbrowserpath.setText(chooser.getSelectedFile().getAbsolutePath());
        });

        settingslanguagelabel = new JLabel(PublicValues.language.translate("ui.settings.language"));
        settingslanguagelabel.setBounds(6, 118, 370, 20);
        settingslanguagelabel.setFont(new Font(settingslanguagelabel.getFont().getName(), Font.BOLD, settingslanguagelabel.getFont().getSize()));
        settingsuiborder.add(settingslanguagelabel);

        settingslanguagelabel.setForeground(PublicValues.globalFontColor);

        settingslanguageselect = new JComboBox();
        settingslanguageselect.setBounds(159, 146, 217, 27);
        settingsuiborder.add(settingslanguageselect);

        settingslanguageselectlabel = new JLabel(PublicValues.language.translate("ui.settings.langselect"));
        settingslanguageselectlabel.setBounds(6, 150, 140, 16);
        settingsuiborder.add(settingslanguageselectlabel);

        settingslanguageselectlabel.setForeground(PublicValues.globalFontColor);

        ArrayList<String> selectcache = new ArrayList<>();

        for(libLanguage.Language language : libLanguage.Language.values()) {
            if(selectcache.contains(language.getName())) {
                continue;
            }
            if(new Resources(true).readToInputStream("lang/" + language.getCode() + ".json") != null) {
                selectcache.add(language.getName());
            }
        }

        for(String s : selectcache) {
            ((DefaultComboBoxModel) settingslanguageselect.getModel()).addElement(s);
        }

        settingslanguageselect.getModel().setSelectedItem(PublicValues.config.getString(ConfigValues.language.name));
        settingsdisableexceptions.setSelected(PublicValues.config.getBoolean(ConfigValues.hideExceptions.name));
        settingsuidisableplayerstats.setSelected(PublicValues.config.getBoolean(ConfigValues.disableplayerstats.name));
        settingsbrowserpath.setText(PublicValues.config.getString(ConfigValues.mypalpath.name));
        settingsuiselecttheme.getModel().setSelectedItem(PublicValues.config.getString(ConfigValues.theme.name));
        settingsplaybackselectquality.getModel().setSelectedItem(PublicValues.config.getString(ConfigValues.audioquality.name));
        settingsturnoffspotifyconnect.setSelected(PublicValues.config.getBoolean(ConfigValues.spconnect.name));

        if(settingslanguageselect.getModel().getSelectedItem().toString().equals(ConfigValues.language.name)) {
            settingslanguageselect.getModel().setSelectedItem(PublicValues.language.translate("ui.settings.nolang"));
        }

        if(!PublicValues.config.getString(ConfigValues.lastfmusername.name).isEmpty()) {
            settingslastfmlogout.setEnabled(true);
            settingslastfmlogin.setEnabled(false);
        }else{
            settingslastfmlogin.setEnabled(true);
            settingslastfmlogout.setEnabled(false);
        }
    }

    public static void applySettings() {
        switch ((String)settingsplaybackselectquality.getModel().getSelectedItem()) {
            case "Normal":
                PublicValues.config.write(ConfigValues.audioquality.name, "NORMAL");
                break;
            case "High":
                PublicValues.config.write(ConfigValues.audioquality.name, "HIGH");
                break;
            case "Very_High":
                PublicValues.config.write(ConfigValues.audioquality.name, "VERY_HIGH");
                break;
        }
        PublicValues.config.write(ConfigValues.spconnect.name, settingsturnoffspotifyconnect.isSelected());
        PublicValues.config.write(ConfigValues.language.name, settingslanguageselect.getModel().getSelectedItem().toString());
        PublicValues.config.write(ConfigValues.hideExceptions.name, settingsdisableexceptions.isSelected());
        PublicValues.config.write(ConfigValues.theme.name, settingsuiselecttheme.getModel().getSelectedItem().toString().split(" from ")[0]);
        PublicValues.config.write(ConfigValues.mypalpath.name, settingsbrowserpath.getText());
        PublicValues.config.write(ConfigValues.disableplayerstats.name, settingsuidisableplayerstats.isSelected());
        JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("ui.settings.pleaserestart"), PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
    }

    public static void triggerUninstall() {
        ProcessBuilder builder;
        try {
            Files.copy(new Resources().readToInputStream("JavaSetupTool.jar"), Paths.get(PublicValues.tempPath + File.separator + "SpotifyXP-Uninstaller.jar"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(PublicValues.appLocation + File.separator + "uninstaller.xml"), Paths.get(PublicValues.tempPath + File.separator + "uninstaller.xml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            builder = new ProcessBuilder("cmd.exe", "/c", "\"" + System.getProperty("java.home") + "/bin/java\"" + " -jar " + PublicValues.tempPath + "/SpotifyXP-Uninstaller.jar");
        }else{
            builder = new ProcessBuilder("bash", "-c", "\"" + System.getProperty("java.home") + "/bin/java\"" + " -jar " + PublicValues.tempPath + "/SpotifyXP-Uninstaller.jar");
        }
        try {
            builder.start();
        } catch (IOException e) {
            ConsoleLogging.Throwable(e);
        }
        System.exit(0);
    }
}
