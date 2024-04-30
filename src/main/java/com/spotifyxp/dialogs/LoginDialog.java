package com.spotifyxp.dialogs;

import com.spotifyxp.Initiator;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.swingextension.CustomLengthPasswordField;
import com.spotifyxp.swingextension.CustomLengthTextField;
import com.spotifyxp.utils.AsyncActionListener;
import com.spotifyxp.utils.StartupTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//ToDo: Check if this is the first start of SpotifyXP and when not support translation

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton spotifyokbutton;
    private JButton spotifycancelbutton;
    private JButton facebook;
    private JLabel spotifylabelusername;
    private JLabel spotifylabelpassword;
    private CustomLengthTextField spotifyusernamefield;
    private CustomLengthPasswordField usernamepasswordfield;

    public LoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(350, 356));
        setResizable(false);

        spotifyusernamefield.setColumns(10);

        usernamepasswordfield.setColumns(10);

        facebook.setText("Facebook");
        facebook.setEnabled(false);
        facebook.setToolTipText("Facebook auth not supported"); //https://github.com/SpotifyXP/SpotifyXP/issues/15
        facebook.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.config.write(ConfigValues.facebook.name, true);
                SplashPanel.frame.setAlwaysOnTop(false);
                PublicValues.facebookcanceldialog = new CancelDialog();
                PublicValues.facebookcanceldialog.showIt();
                dispose();
            }
        }));

        getRootPane().setDefaultButton(spotifyokbutton);

        spotifyokbutton.setText("Ok"); //ToDo: Translate
        spotifyokbutton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.config.write(ConfigValues.username.name, spotifyusernamefield.getText());
                PublicValues.config.write(ConfigValues.password.name, new String(usernamepasswordfield.getPassword()));
                PublicValues.config.write(ConfigValues.facebook.name, false);
                dispose();
            }
        }));

        spotifycancelbutton.setText("Cancel"); //ToDo: Translate
        spotifycancelbutton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }));

        spotifylabelusername.setText("E-Mail"); //ToDo: Translate

        spotifylabelpassword.setText("Password"); //ToDo: Translate
    }

    public void openWithInvalidAuth() {
        setTitle("Invalid Login! Try again"); //ToDo: Translate
        getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.red);
        Initiator.past = true;
        SplashPanel.frame.setAlwaysOnTop(false);
        pack();
        setVisible(true);
        Initiator.past = false;
        Initiator.startupTime = new StartupTime();
        Initiator.thread.start();
        SplashPanel.frame.setAlwaysOnTop(true);
    }

    public void open() {
        setTitle("Enter Spotify Credentials"); //ToDo: Translate
        Initiator.past = true;
        SplashPanel.frame.setAlwaysOnTop(false);
        pack();
        setVisible(true);
        Initiator.past = false;
        Initiator.startupTime = new StartupTime();
        Initiator.thread.start();
        SplashPanel.frame.setAlwaysOnTop(true);
    }

    public static void main(String[] args) {
        LoginDialog dialog = new LoginDialog();
        dialog.open();
    }

    private void createUIComponents() {
        spotifyusernamefield = new CustomLengthTextField(254); //https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
        usernamepasswordfield = new CustomLengthPasswordField(500); //https://community.spotify.com/t5/Accounts/max-password-lenght/td-p/5533953
    }
}
