package com.spotifyxp.panels;

import com.spotifyxp.PublicValues;
import com.spotifyxp.utils.ConnectionUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;
import java.net.URISyntaxException;

public class Feedback extends JPanel implements View {
    public static JLabel feedbackMakeSureLabel;
    public static JPanel feedbackIssuePanel;
    public static JButton feedbackViewIssuesButton;
    public static JButton feedbackCreateIssueButton;
    public static JButton feedbackGitHubButton;

    public Feedback() {
        setLayout(null);
        setVisible(false);

        feedbackMakeSureLabel = new JLabel(PublicValues.language.translate("ui.feedback.makesure"));
        feedbackMakeSureLabel.setBounds(10, 23, 690, 25);
        add(feedbackMakeSureLabel);
        feedbackMakeSureLabel.setForeground(PublicValues.globalFontColor);

        feedbackIssuePanel = new JPanel();
        feedbackIssuePanel.setBorder(new TitledBorder(null, PublicValues.language.translate("ui.feedback.issues.border"), TitledBorder.LEADING, TitledBorder.TOP, null, PublicValues.globalFontColor));
        feedbackIssuePanel.setBounds(0, 313, 426, 88);
        add(feedbackIssuePanel);
        feedbackIssuePanel.setLayout(null);

        feedbackViewIssuesButton = new JButton(PublicValues.language.translate("ui.feedback.issues.view"));
        feedbackViewIssuesButton.setBounds(10, 21, 188, 56);
        feedbackIssuePanel.add(feedbackViewIssuesButton);
        feedbackViewIssuesButton.setForeground(PublicValues.globalFontColor);
        feedbackViewIssuesButton.addActionListener(e -> {
            try {
                ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP/issues");
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        feedbackCreateIssueButton = new JButton(PublicValues.language.translate("ui.feedback.issues.create"));
        feedbackCreateIssueButton.setBounds(227, 21, 188, 56);
        feedbackIssuePanel.add(feedbackCreateIssueButton);
        feedbackCreateIssueButton.setForeground(PublicValues.globalFontColor);
        feedbackCreateIssueButton.addActionListener(e -> {
            try {
                ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP/issues/new");
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        feedbackGitHubButton = new JButton(PublicValues.language.translate("ui.feedback.github.open"));
        feedbackGitHubButton.setBounds(466, 335, 250, 55);
        add(feedbackGitHubButton);
        feedbackGitHubButton.setForeground(PublicValues.globalFontColor);
        feedbackGitHubButton.addActionListener(e -> {
            try {
                ConnectionUtils.openBrowser("https://github.com/SpotifyXP/SpotifyXP");
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void makeVisible() {
        setVisible(true);
    }

    @Override
    public void makeInvisible() {
        setVisible(false);
    }
}
