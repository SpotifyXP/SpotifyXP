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

        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(350, 356));
        setResizable(false);

        spotifyusernamefield = new CustomLengthTextField(254); //https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
        spotifyusernamefield.setBounds(10, 81, 314, 39);
        spotifyusernamefield.setColumns(10);
        add(spotifyusernamefield);

        usernamepasswordfield = new CustomLengthPasswordField(500); //https://community.spotify.com/t5/Accounts/max-password-lenght/td-p/5533953
        usernamepasswordfield.setColumns(10);
        usernamepasswordfield.setBounds(10, 179, 314, 39);
        add(usernamepasswordfield);

        facebook = new JButton();
        facebook.setText("Facebook");
        facebook.setBounds(10, 228, 314, 20);
        facebook.setEnabled(false);
        facebook.setToolTipText("Facebook auth not supported"); //https://github.com/SpotifyXP/SpotifyXP/issues/15
        //add(facebook);
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

        spotifyokbutton = new JButton();
        spotifyokbutton.setText("Ok"); //ToDo: Translate
        spotifyokbutton.setBounds(10, 275, 139, 31);
        add(spotifyokbutton);
        spotifyokbutton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.config.write(ConfigValues.username.name, spotifyusernamefield.getText());
                PublicValues.config.write(ConfigValues.password.name, new String(usernamepasswordfield.getPassword()));
                PublicValues.config.write(ConfigValues.facebook.name, false);
                dispose();
            }
        }));

        spotifycancelbutton = new JButton();
        spotifycancelbutton.setText("Cancel"); //ToDo: Translate
        spotifycancelbutton.setBounds(185, 275, 139, 31);
        add(spotifycancelbutton);
        spotifycancelbutton.addActionListener(new AsyncActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }));

        spotifylabelusername = new JLabel();
        spotifylabelusername.setText("E-Mail"); //ToDo: Translate
        spotifylabelusername.setBounds(10, 60, 111, 14);
        add(spotifylabelusername);

        spotifylabelpassword = new JLabel();
        spotifylabelpassword.setText("Password"); //ToDo: Translate
        spotifylabelpassword.setBounds(10, 158, 111, 14);
        add(spotifylabelpassword);
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
}
