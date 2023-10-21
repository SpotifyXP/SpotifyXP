package com.spotifyxp.lastfm;

import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.deps.de.umass.lastfm.Authenticator;
import com.spotifyxp.swingextension.CustomLengthPasswordField;
import com.spotifyxp.swingextension.CustomLengthTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("Convert2Lambda")
public class LastFMLogin {
    private static CustomLengthTextField lastfmusernamefield;
    private static CustomLengthPasswordField usernamepasswordfield;
    private static JButton lastfmokbutton;
    private static JButton lastfmcancelbutton;
    private static class ContentPanel extends JPanel {
        public ContentPanel() {
            lastfmusernamefield = new CustomLengthTextField(60);
            lastfmusernamefield.setBounds(10, 81, 314, 39);
            add(lastfmusernamefield);
            lastfmusernamefield.setColumns(10);
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setLayout(new BorderLayout());
            usernamepasswordfield = new CustomLengthPasswordField(60);
            usernamepasswordfield.setColumns(10);
            usernamepasswordfield.setBounds(10, 179, 314, 39);
            add(usernamepasswordfield);
            lastfmokbutton = new JButton("Ok");
            lastfmokbutton.setBounds(10, 275, 139, 31);
            add(lastfmokbutton);
            lastfmcancelbutton = new JButton("Cancel");
            lastfmcancelbutton.setBounds(185, 275, 139, 31);
            add(lastfmcancelbutton);
            JLabel lastfmlabelusername = new JLabel("Username");
            lastfmlabelusername.setBounds(10, 60, 111, 14);
            add(lastfmlabelusername);
            JLabel lastfmlabelpassword = new JLabel("Password");
            lastfmlabelpassword.setBounds(10, 158, 111, 14);
            add(lastfmlabelpassword);
            JLabel lastfmlabelinfo = new JLabel("Please enter your Last.fm credentials");
            lastfmlabelinfo.setHorizontalAlignment(SwingConstants.CENTER);
            lastfmlabelinfo.setBounds(10, 11, 314, 14);
            add(lastfmlabelinfo);
            lastfmlabelinfo.setVisible(false);
            usernamepasswordfield.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        lastfmokbutton.doClick();
                    }
                }
            });
        }
    }
    public void openWithInvalidAuth(Runnable runnable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Invalid Login! Try again");
        dialog.getContentPane().add(new ContentPanel());
        dialog.setResizable(false);
        dialog.setPreferredSize(new Dimension(350, 356));
        dialog.pack();
        dialog.setVisible(true);
        dialog.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.red);
        lastfmokbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Authenticator.getMobileSession(lastfmusernamefield.getText(), new String(usernamepasswordfield.getPassword()), LFMValues.apikey, LFMValues.apisecret).getKey();
                    PublicValues.config.write(ConfigValues.lastfmusername.name, lastfmusernamefield.getText());
                    PublicValues.config.write(ConfigValues.lastfmpassword.name, new String(usernamepasswordfield.getPassword()));
                }catch (NullPointerException ex) {
                    openWithInvalidAuth(runnable);
                    dialog.dispose();
                    return;
                }
                dialog.dispose();
                runnable.run();
            }
        });
        lastfmcancelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }

    public void open(Runnable runnable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Enter Last.fm Credentials");
        dialog.getContentPane().add(new ContentPanel());
        dialog.setResizable(false);
        dialog.setPreferredSize(new Dimension(350, 356));
        dialog.pack();
        dialog.setVisible(true);
        lastfmokbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Authenticator.getMobileSession(lastfmusernamefield.getText(), new String(usernamepasswordfield.getPassword()), LFMValues.apikey, LFMValues.apisecret).getKey();
                    PublicValues.config.write(ConfigValues.lastfmusername.name, lastfmusernamefield.getText());
                    PublicValues.config.write(ConfigValues.lastfmpassword.name, new String(usernamepasswordfield.getPassword()));
                }catch (NullPointerException ex) {
                    openWithInvalidAuth(runnable);
                    dialog.dispose();
                    return;
                }
                dialog.dispose();
                runnable.run();
            }
        });
        lastfmcancelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }
}
