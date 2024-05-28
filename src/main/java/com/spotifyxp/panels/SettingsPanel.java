package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.args.CustomSaveDir;
import com.spotifyxp.configuration.Config;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.exception.ElementNotFoundException;
import com.spotifyxp.lib.libLanguage;
import com.spotifyxp.swingextension.JFrame;
import com.spotifyxp.theming.Theme;
import com.spotifyxp.theming.ThemeLoader;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.GraphicalMessage;
import com.spotifyxp.utils.Resources;
import com.spotifyxp.utils.Utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Style;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class SettingsPanel {
    private static ArrayList<Object[]> elements;
    private JPanel contentPanel;
    private JTabbedPane switcher;
    private JTextField settingsBrowserPath;
    private JButton settingsBrowserPathChoose;
    private JLabel settingsBrowserPathLabel;
    private JPanel uiSettings;
    private JPanel browserSettings;
    private JPanel playbackSettings;
    private JPanel otherSettings;
    private JRadioButton uiHideExceptions;
    private JComboBox uiLanguageSelect;
    private JLabel uiLanguageLabel;
    private JLabel uiLanguageSelectLabel;
    private JComboBox uiThemeSelect;
    private JLabel uiThemeSelectLabel;
    private JComboBox playbackQualitySelect;
    private JRadioButton playbackDisableSpotifyConnect;
    private JLabel playbackQualitySelectLabel;
    private JCheckBox otherAutoPlayEnabled;
    private JTextField otherCrossfadeDuration;
    private JCheckBox otherPreloadEnabled;
    private JTextField otherReleaseLineDelay;
    private JCheckBox otherBypassSinkVolume;
    private JTextField otherPreferredLocale;
    private JLabel otherAutoPlayEnabledLabel;
    private JLabel otherCrossfadeDurationLabel;
    private JCheckBox otherEnableNormalization;
    private JLabel otherEnableNormalizationLabel;
    private JTextField otherNormalizationPregain;
    private JLabel otherNormalizationPregainLabel;
    private JTextField otherMixerSearchKeywords;
    private JLabel otherMixerSearchKeywordsLabel;
    private JLabel otherPreloadEnabledLabel;
    private JLabel otherReleaseLineDelayLabel;
    private JLabel otherBypassSinkVolumeLabel;
    private JLabel otherPreferredLocaleLabel;

    public SettingsPanel() {
        initBrowserSettings();
        initUISettings();
        initPlaybackSettings();
        initOtherSettings();

        //Show warning message on 'Other' tab
        switcher.addChangeListener(e -> {
            if(switcher.getSelectedIndex() == switcher.getTabCount() - 1) {
                JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("ui.settings.other.message"), PublicValues.language.translate("ui.settings.other.message.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            }
        });
        //----

        //Set color of switcher
        switcher.setForeground(PublicValues.globalFontColor);
        //----

        elements = new ArrayList<>();
        addAllToElementList();
    }

    private void initBrowserSettings() {
        settingsBrowserPathLabel.setText(PublicValues.language.translate("ui.settings.browser.label"));
        settingsBrowserPathLabel.setForeground(PublicValues.globalFontColor);

        settingsBrowserPath.setForeground(PublicValues.globalFontColor);
        settingsBrowserPath.setText(PublicValues.config.getString(ConfigValues.mypalpath.name));

        settingsBrowserPathChoose.setForeground(PublicValues.globalFontColor);
        settingsBrowserPathChoose.addActionListener(new AsyncActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter filter = new FileNameExtensionFilter(PublicValues.language.translate("ui.settings.mypal.path.filter"),"exe");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle(PublicValues.language.translate("ui.settings.mypal.path.choose"));
            chooser.showOpenDialog(contentPanel);
            if(chooser.getSelectedFile() != null) settingsBrowserPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }));
    }

    private void initUISettings() {
        uiHideExceptions.setText(PublicValues.language.translate("general.exception.hide"));
        uiHideExceptions.setSelected(PublicValues.config.getBoolean(ConfigValues.hideExceptions.name));
        uiHideExceptions.setForeground(PublicValues.globalFontColor);

        uiThemeSelectLabel.setText(PublicValues.language.translate("ui.settings.theme"));
        uiThemeSelectLabel.setForeground(PublicValues.globalFontColor);

        uiThemeSelect.setForeground(PublicValues.globalFontColor);
        for(Theme theme : ThemeLoader.getAvailableThemes()) {
            ((DefaultComboBoxModel)uiThemeSelect.getModel()).addElement(Utils.getClassName(theme.getClass()) + " from " + theme.getAuthor());
        }
        uiThemeSelect.setSelectedItem(PublicValues.config.getString(ConfigValues.theme.name));

        uiLanguageLabel.setText(PublicValues.language.translate("ui.settings.language"));
        uiLanguageLabel.setForeground(PublicValues.globalFontColor);

        uiLanguageSelectLabel.setText(PublicValues.language.translate("ui.settings.langselect"));
        uiLanguageSelectLabel.setForeground(PublicValues.globalFontColor);

        uiLanguageSelect.setForeground(PublicValues.globalFontColor);
        ArrayList<String> selectcache = new ArrayList<>();
        for(libLanguage.Language language : libLanguage.Language.values()) {
            if(selectcache.contains(language.getName())) {
                continue;
            }
            if(new Resources(true).readToInputStream("lang/" + language.getCode() + ".json") != null) {
                ((DefaultComboBoxModel) uiLanguageSelect.getModel()).addElement(language.getName());
            }
        }
        uiLanguageSelect.setSelectedItem(PublicValues.config.getString(ConfigValues.language.name));
    }

    private void initPlaybackSettings() {
        playbackQualitySelect.setForeground(PublicValues.globalFontColor);
        playbackQualitySelect.setModel(new DefaultComboBoxModel(new String[]{"Normal", "High", "Very_High"}));
        playbackQualitySelect.setSelectedItem(PublicValues.config.getString(ConfigValues.audioquality.name));

        playbackQualitySelectLabel.setForeground(PublicValues.globalFontColor);
        playbackQualitySelectLabel.setText(PublicValues.language.translate("ui.settings.quality"));

        playbackDisableSpotifyConnect.setForeground(PublicValues.globalFontColor);
        playbackDisableSpotifyConnect.setSelected(PublicValues.config.getBoolean(ConfigValues.spconnect.name));
        playbackDisableSpotifyConnect.setText(PublicValues.language.translate("ui.settings.spconnect"));
    }

    private void initOtherSettings() {
        otherAutoPlayEnabled.setText("");
        otherAutoPlayEnabled.setSelected(PublicValues.config.getBoolean(ConfigValues.other_autoplayenabled.name));
        otherAutoPlayEnabled.setForeground(PublicValues.globalFontColor);

        otherAutoPlayEnabledLabel.setText(PublicValues.language.translate("ui.settings.other.autoplayenabled"));
        otherAutoPlayEnabledLabel.setForeground(PublicValues.globalFontColor);

        otherCrossfadeDuration.setForeground(PublicValues.globalFontColor);
        otherCrossfadeDuration.setText(String.valueOf(PublicValues.config.getInt(ConfigValues.other_crossfadeduration.name)));

        otherCrossfadeDurationLabel.setText(PublicValues.language.translate("ui.settings.other.crossfadeduration"));
        otherCrossfadeDurationLabel.setForeground(PublicValues.globalFontColor);

        otherEnableNormalization.setText("");
        otherEnableNormalization.setSelected(PublicValues.config.getBoolean(ConfigValues.other_enablenormalization.name));
        otherEnableNormalization.setForeground(PublicValues.globalFontColor);

        otherEnableNormalizationLabel.setText(PublicValues.language.translate("ui.settings.other.enablenormalization"));
        otherEnableNormalizationLabel.setForeground(PublicValues.globalFontColor);

        otherNormalizationPregain.setForeground(PublicValues.globalFontColor);
        otherNormalizationPregain.setText(String.valueOf(PublicValues.config.getInt(ConfigValues.other_normalizationpregain.name)));

        otherNormalizationPregainLabel.setText(PublicValues.language.translate("ui.settings.other.normalizationpregain"));
        otherNormalizationPregainLabel.setForeground(PublicValues.globalFontColor);

        otherMixerSearchKeywords.setForeground(PublicValues.globalFontColor);
        otherMixerSearchKeywords.setText(PublicValues.config.getString(ConfigValues.other_mixersearchkeywords.name));

        otherMixerSearchKeywordsLabel.setText(PublicValues.language.translate("ui.settings.other.mixersearchkeywords"));
        otherMixerSearchKeywordsLabel.setForeground(PublicValues.globalFontColor);

        otherPreloadEnabled.setText("");
        otherPreloadEnabled.setSelected(PublicValues.config.getBoolean(ConfigValues.other_preloadenabled.name));
        otherPreloadEnabled.setForeground(PublicValues.globalFontColor);

        otherPreloadEnabledLabel.setText(PublicValues.language.translate("ui.settings.other.preloadenabled"));
        otherPreloadEnabledLabel.setForeground(PublicValues.globalFontColor);

        otherReleaseLineDelay.setForeground(PublicValues.globalFontColor);
        otherReleaseLineDelay.setText(String.valueOf(PublicValues.config.getInt(ConfigValues.other_releaselinedelay.name)));

        otherReleaseLineDelayLabel.setText(PublicValues.language.translate("ui.settings.other.releaselinedelay"));
        otherReleaseLineDelayLabel.setForeground(PublicValues.globalFontColor);

        otherBypassSinkVolume.setText("");
        otherBypassSinkVolume.setSelected(PublicValues.config.getBoolean(ConfigValues.other_bypasssinkvolume.name));
        otherBypassSinkVolume.setForeground(PublicValues.globalFontColor);

        otherBypassSinkVolumeLabel.setText(PublicValues.language.translate("ui.settings.other.bypasssinkvolume"));
        otherBypassSinkVolumeLabel.setForeground(PublicValues.globalFontColor);

        otherPreferredLocale.setForeground(PublicValues.globalFontColor);
        otherPreferredLocale.setText(PublicValues.config.getString(ConfigValues.other_preferredlocale.name));

        otherPreferredLocaleLabel.setText(PublicValues.language.translate("ui.settings.other.preferredlocale"));
        otherPreferredLocaleLabel.setForeground(PublicValues.globalFontColor);
    }

    private void addAllToElementList() {
        addToElementList("contentPanel", contentPanel);
        addToElementList("switcher", switcher);
        addToElementList("settingsBrowserPath", settingsBrowserPath);
        addToElementList("settingsBrowserPathChoose", settingsBrowserPathChoose);
        addToElementList("settingsBrowserPathLabel", settingsBrowserPathLabel);
        addToElementList("uiSettings", uiSettings);
        addToElementList("browserSettings", browserSettings);
        addToElementList("playbackSettings", playbackSettings);
        addToElementList("otherSettings", otherSettings);
        addToElementList("uiHideExceptions", uiHideExceptions);
        addToElementList("uiLanguageSelect", uiLanguageSelect);
        addToElementList("uiLanguageLabel", uiLanguageLabel);
        addToElementList("uiLanguageSelectLabel", uiLanguageSelectLabel);
        addToElementList("uiThemeSelect", uiThemeSelect);
        addToElementList("uiThemeSelectLabel", uiThemeSelectLabel);
        addToElementList("playbackQualitySelect", playbackQualitySelect);
        addToElementList("playbackDisableSpotifyConnect", playbackDisableSpotifyConnect);
        addToElementList("playbackQualitySelectLabel", playbackQualitySelectLabel);
        addToElementList("otherAutoPlayEnabled", otherAutoPlayEnabled);
        addToElementList("otherCrossfadeDuration", otherCrossfadeDuration);
        addToElementList("otherPreloadEnabled", otherPreloadEnabled);
        addToElementList("otherReleaseLineDelay", otherReleaseLineDelay);
        addToElementList("otherBypassSinkVolume", otherBypassSinkVolume);
        addToElementList("otherPreferredLocale", otherPreferredLocale);
        addToElementList("otherAutoPlayEnabledLabel", otherAutoPlayEnabledLabel);
        addToElementList("otherCrossfadeDurationLabel", otherCrossfadeDurationLabel);
        addToElementList("otherEnableNormalization", otherEnableNormalization);
        addToElementList("otherEnableNormalizationLabel", otherEnableNormalizationLabel);
        addToElementList("otherNormalizationPregain", otherNormalizationPregain);
        addToElementList("otherNormalizationPregainLabel", otherNormalizationPregainLabel);
        addToElementList("otherMixerSearchKeywords", otherMixerSearchKeywords);
        addToElementList("otherMixerSearchKeywordsLabel", otherMixerSearchKeywordsLabel);
        addToElementList("otherPreloadEnabledLabel", otherPreloadEnabledLabel);
        addToElementList("otherReleaseLineDelayLabel", otherReleaseLineDelayLabel);
        addToElementList("otherBypassSinkVolumeLabel", otherBypassSinkVolumeLabel);
        addToElementList("otherPreferredLocaleLabel", otherPreferredLocaleLabel);
    }

    private void addToElementList(String name, Object instance) {
        elements.add(new Object[]{name, instance});
    }

    public static JPanel getContainer() {
        return getElementByNameAutoThrow("contentPanel", JPanel.class);
    }

    public static <E> E getElementByName(String name, Class<E> elementType) throws ElementNotFoundException {
        for(Object[] element : elements) {
            if(element[0].equals(name)) {
                return elementType.cast(element[1]);
            }
        }
        throw new ElementNotFoundException(elementType);
    }

    public static <E> E getElementByNameAutoThrow(String name, Class<E> elementType) {
        for(Object[] element : elements) {
            if(element[0].equals(name)) {
                return elementType.cast(element[1]);
            }
        }
        throw new RuntimeException(new ElementNotFoundException(elementType));
    }


    private static void saveSettings() {
        try {
            switch ((String) getElementByNameAutoThrow("playbackQualitySelect", JComboBox.class).getModel().getSelectedItem()) {
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
            PublicValues.config.write(ConfigValues.spconnect.name, getElementByNameAutoThrow("playbackDisableSpotifyConnect", JCheckBox.class).isSelected());
            PublicValues.config.write(ConfigValues.language.name, getElementByNameAutoThrow("uiLanguageSelect", JComboBox.class).getModel().getSelectedItem().toString());
            PublicValues.config.write(ConfigValues.hideExceptions.name, getElementByNameAutoThrow("uiHideExceptions", JCheckBox.class).isSelected());
            PublicValues.config.write(ConfigValues.theme.name, getElementByNameAutoThrow("uiThemeSelect", JComboBox.class).getModel().getSelectedItem().toString().split(" from ")[0]);
            PublicValues.config.write(ConfigValues.mypalpath.name, getElementByNameAutoThrow("settingsBrowserPath", JTextField.class).getText());
            PublicValues.config.write(ConfigValues.other_autoplayenabled.name, getElementByNameAutoThrow("otherAutoPlayEnabled", JCheckBox.class).isSelected());
            PublicValues.config.write(ConfigValues.other_crossfadeduration.name, Integer.parseInt(getElementByNameAutoThrow("otherCrossfadeDuration", JTextField.class).getText()));
            PublicValues.config.write(ConfigValues.other_enablenormalization.name, getElementByNameAutoThrow("otherEnableNormalization", JCheckBox.class).isSelected());
            PublicValues.config.write(ConfigValues.other_normalizationpregain.name, Integer.parseInt(getElementByNameAutoThrow("otherNormalizationPregain", JTextField.class).getText()));
            PublicValues.config.write(ConfigValues.other_mixersearchkeywords.name, getElementByNameAutoThrow("otherMixerSearchKeywords", JTextField.class).getText());
            PublicValues.config.write(ConfigValues.other_preloadenabled.name, getElementByNameAutoThrow("otherPreloadEnabled", JCheckBox.class).isSelected());
            PublicValues.config.write(ConfigValues.other_releaselinedelay.name, Integer.parseInt(getElementByNameAutoThrow("otherReleaseLineDelay", JTextField.class).getText()));
            PublicValues.config.write(ConfigValues.other_bypasssinkvolume.name, getElementByNameAutoThrow("otherBypassSinkVolume", JCheckBox.class).isSelected());
            PublicValues.config.write(ConfigValues.other_preferredlocale.name, getElementByNameAutoThrow("otherPreferredLocale", JTextField.class).getText());
            JOptionPane.showConfirmDialog(ContentPanel.frame, PublicValues.language.translate("ui.settings.pleaserestart"), PublicValues.language.translate("joptionpane.info"), JOptionPane.OK_CANCEL_OPTION);
        }catch (NumberFormatException e) {
            GraphicalMessage.sorryError("Failed to write settings");
        }
    }


    public static void open() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveSettings();
                e.getWindow().dispose();
            }
        });
        frame.setTitle(PublicValues.language.translate("ui.settings.title"));
        frame.getContentPane().add(SettingsPanel.getContainer());
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }
}
