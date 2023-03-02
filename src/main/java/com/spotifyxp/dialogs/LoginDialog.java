package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog {
    private static JTextField spotifyusernamefield;
    private static JTextField usernamepasswordfield;
    private static JButton spotifyokbutton;
    private static JButton spotifycancelbutton;
    private static class ContentPanel extends JPanel {
        public ContentPanel() {
            spotifyusernamefield = new JTextField();
            spotifyusernamefield.setBounds(10, 81, 314, 39);
            add(spotifyusernamefield);
            spotifyusernamefield.setColumns(10);

            usernamepasswordfield = new JTextField();
            usernamepasswordfield.setColumns(10);
            usernamepasswordfield.setBounds(10, 179, 314, 39);
            add(usernamepasswordfield);
            spotifyokbutton = new JButton("Ok");
            spotifyokbutton.setBounds(10, 275, 139, 31);
            add(spotifyokbutton);
            spotifycancelbutton = new JButton("Cancel");
            spotifycancelbutton.setBounds(185, 275, 139, 31);
            add(spotifycancelbutton);
            JLabel spotifylabelusername = new JLabel("Username");
            spotifylabelusername.setBounds(10, 60, 111, 14);
            add(spotifylabelusername);
            JLabel spotifylabelpassword = new JLabel("Password");
            spotifylabelpassword.setBounds(10, 158, 111, 14);
            add(spotifylabelpassword);
            JLabel spotifylabelinfo = new JLabel("Please enter your Spotify credentials");
            spotifylabelinfo.setHorizontalAlignment(SwingConstants.CENTER);
            spotifylabelinfo.setBounds(10, 11, 314, 14);
            add(spotifylabelinfo);
        }
    }
    public void open() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Enter Spotify Credentials");
        dialog.getContentPane().add(new ContentPanel());
        dialog.setResizable(false);
        dialog.setPreferredSize(new Dimension(350, 356));
        dialog.pack();
        dialog.setVisible(true);
        spotifyokbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicValues.config.write(ConfigValues.username.name, spotifyusernamefield.getText());
                PublicValues.config.write(ConfigValues.password.name, usernamepasswordfield.getText());
                dialog.dispose();
            }
        });
        spotifycancelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
