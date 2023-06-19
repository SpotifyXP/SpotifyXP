package com.spotifyxp.dialogs;
import com.spotifyxp.Initiator;
import com.spotifyxp.PublicValues;
import com.spotifyxp.configuration.ConfigValues;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.panels.SplashPanel;
import com.spotifyxp.swingextension.CustomLengthTextField;
import com.spotifyxp.utils.StartupTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

@SuppressWarnings("Convert2Lambda")
public class LoginDialog {
    private static CustomLengthTextField spotifyusernamefield;
    private static CustomLengthTextField usernamepasswordfield;
    private static JButton spotifyokbutton;
    private static JButton spotifycancelbutton;
    private static class ContentPanel extends JPanel {
        public ContentPanel() {
            spotifyusernamefield = new CustomLengthTextField(60);
            spotifyusernamefield.setBounds(10, 81, 314, 39);
            add(spotifyusernamefield);
            spotifyusernamefield.setColumns(10);
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setLayout(new BorderLayout());
            usernamepasswordfield = new CustomLengthTextField(60);
            usernamepasswordfield.setColumns(10);
            usernamepasswordfield.setBounds(10, 179, 314, 39);
            add(usernamepasswordfield);
            spotifyokbutton = new JButton("Ok");
            spotifyokbutton.setBounds(10, 275, 139, 31);
            add(spotifyokbutton);
            spotifycancelbutton = new JButton("Cancel");
            spotifycancelbutton.setBounds(185, 275, 139, 31);
            add(spotifycancelbutton);
            JLabel spotifylabelusername = new JLabel("E-Mail");
            spotifylabelusername.setBounds(10, 60, 111, 14);
            add(spotifylabelusername);
            JLabel spotifylabelpassword = new JLabel("Password");
            spotifylabelpassword.setBounds(10, 158, 111, 14);
            add(spotifylabelpassword);
            JLabel spotifylabelinfo = new JLabel("Please enter your Spotify credentials");
            spotifylabelinfo.setHorizontalAlignment(SwingConstants.CENTER);
            spotifylabelinfo.setBounds(10, 11, 314, 14);
            add(spotifylabelinfo);
            spotifylabelinfo.setVisible(false);
            usernamepasswordfield.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        spotifyokbutton.doClick();
                    }
                }
            });
        }
    }
    public void openWithInvalidAuth() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Invalid Login! Try again");
        dialog.getContentPane().add(new ContentPanel());
        dialog.setResizable(false);
        dialog.setPreferredSize(new Dimension(350, 356));
        dialog.pack();
        dialog.setVisible(true);
        dialog.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.red);
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
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Initiator.past = true;
        SplashPanel.frame.setAlwaysOnTop(false);
        while(dialog.isVisible()) {
            try {
                Thread.sleep(99);
            } catch (InterruptedException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
        Initiator.past = false;
        Initiator.startupTime = new StartupTime();
        Initiator.thread.start();
        SplashPanel.frame.setAlwaysOnTop(true);
    }
    @SuppressWarnings("BusyWait")
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
                if(!new File(PublicValues.fileslocation + "/" + "LOCKED").delete()) {
                    ConsoleLogging.error(PublicValues.language.translate("startup.error.lockdelete"));
                }
                System.exit(0);
            }
        });
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Initiator.past = true;
        SplashPanel.frame.setAlwaysOnTop(false);
        while(dialog.isVisible()) {
            try {
                Thread.sleep(99);
            } catch (InterruptedException e) {
                ExceptionDialog.open(e);
                ConsoleLogging.Throwable(e);
            }
        }
        Initiator.past = false;
        Initiator.startupTime = new StartupTime();
        Initiator.thread.start();
        SplashPanel.frame.setAlwaysOnTop(true);
    }
}
