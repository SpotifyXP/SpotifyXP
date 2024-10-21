package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.ConnectionUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;
import java.net.URISyntaxException;

public class Feedback extends JPanel {
    public static JLabel feedbackmakesurelabel;
    public static JPanel feedbackissuepanel;
    public static JButton feedbackviewissuesbutton;
    public static JButton feedbackcreateissuebutton;
    public static JButton feedbackgithubbutton;

    public Feedback() {
        setBounds(0, 0, 784, 421);
        ContentPanel.tabpanel.add(this);
        setLayout(null);
        feedbackmakesurelabel = new JLabel(PublicValues.language.translate("ui.feedback.makesure"));
        feedbackmakesurelabel.setBounds(10, 23, 690, 25);
        add(feedbackmakesurelabel);
        feedbackmakesurelabel.setForeground(PublicValues.globalFontColor);
        feedbackissuepanel = new JPanel();
        feedbackissuepanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.feedback.issues.border"), TitledBorder.LEADING, TitledBorder.TOP, null, PublicValues.globalFontColor));
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
        feedbackgithubbutton.addActionListener(e -> {
            try {
                ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP");
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        feedbackviewissuesbutton.addActionListener(e -> {
            try {
                ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP/issues");
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        feedbackcreateissuebutton.addActionListener(e -> {
            try {
                ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP/issues/new");
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
