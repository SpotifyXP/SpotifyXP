package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.updater.Updater;
import com.spotifyxp.utils.ConnectionUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Feedback extends JPanel {
    public static JTextField feedbackupdaterversionfield;
    public static JLabel feedbackmakesurelabel;
    public static JPanel feedbackissuepanel;
    public static JButton feedbackviewissuesbutton;
    public static JButton feedbackcreateissuebutton;
    public static JButton feedbackgithubbutton;
    public static JPanel feedbackupdatespanel;
    public static JLabel feedbackwillbemovedlabel;
    public static JButton feedbackupdaterdownloadbutton;

    public Feedback() {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        feedbackmakesurelabel = new JLabel(PublicValues.language.translate("ui.feedback.makesure"));
        feedbackmakesurelabel.setBounds(10, 23, 690, 25);
        add(feedbackmakesurelabel);
        feedbackmakesurelabel.setForeground(PublicValues.globalFontColor);
        feedbackissuepanel = new JPanel();
        feedbackissuepanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.feedback.issues.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        feedbackissuepanel.setBounds(0, 333, 426, 88);
        add(feedbackissuepanel);
        feedbackissuepanel.setLayout(null);
        feedbackviewissuesbutton = new JButton(PublicValues.language.translate("ui.feedback.issues.view"));
        feedbackviewissuesbutton.setBounds(10, 21, 188, 56);
        feedbackissuepanel.add(feedbackviewissuesbutton);
        feedbackviewissuesbutton.setForeground(PublicValues.globalFontColor);
        feedbackcreateissuebutton = new JButton(PublicValues.language.translate("ui.feedback.issues.create"));
        feedbackcreateissuebutton.setBounds(227, 21, 188, 56);
        feedbackissuepanel.add(feedbackcreateissuebutton);
        feedbackcreateissuebutton.setForeground(PublicValues.globalFontColor);
        feedbackgithubbutton = new JButton(PublicValues.language.translate("ui.feedback.github.open"));
        feedbackgithubbutton.setBounds(466, 355, 250, 55);
        add(feedbackgithubbutton);
        feedbackgithubbutton.setForeground(PublicValues.globalFontColor);
        feedbackupdatespanel = new JPanel();
        feedbackupdatespanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.updater.border"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        feedbackupdatespanel.setBounds(10, 59, 566, 249);
        add(feedbackupdatespanel);
        feedbackupdatespanel.setLayout(null);
        feedbackupdaterversionfield = new JTextField();
        feedbackupdaterversionfield.setBounds(10, 85, 230, 20);
        feedbackupdatespanel.add(feedbackupdaterversionfield);
        feedbackupdaterversionfield.setColumns(10);
        feedbackwillbemovedlabel = new JLabel("The Updater will be moved to an other place");
        feedbackwillbemovedlabel.setBounds(10, 29, 327, 14);
        feedbackupdatespanel.add(feedbackwillbemovedlabel);
        feedbackwillbemovedlabel.setForeground(PublicValues.globalFontColor);
        feedbackupdaterdownloadbutton = new JButton(PublicValues.language.translate("ui.updater.downloadnewest"));
        feedbackupdaterdownloadbutton.setBounds(10, 149, 230, 23);
        feedbackupdatespanel.add(feedbackupdaterdownloadbutton);
        feedbackupdaterdownloadbutton.setForeground(PublicValues.globalFontColor);
        feedbackupdaterversionfield.setEditable(false);
        feedbackupdaterdownloadbutton.addActionListener(e -> ConnectionUtils.openBrowser(new Updater().updateAvailable().url));
        feedbackgithubbutton.addActionListener(e -> ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP"));
        feedbackviewissuesbutton.addActionListener(e -> ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP/issues"));
        feedbackcreateissuebutton.addActionListener(e -> ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP/issues/new"));
    }
}
