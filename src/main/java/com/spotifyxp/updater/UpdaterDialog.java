package com.spotifyxp.updater;

import com.spotifyxp.PublicValues;
import com.spotifyxp.api.GitHubAPI;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.DoubleArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class UpdaterDialog extends JFrame {
    public UpdaterDialog() {
        JFrame frame = this;
        this.setTitle(PublicValues.language.translate("updater.dialog.title"));
        JPanel contentPane = new JPanel();
        JScrollPane updaterscrollpane = new JScrollPane();
        updaterscrollpane.setBounds(0, 27, 496, 242);
        contentPane.add(updaterscrollpane);

        contentPane.setLayout(null);

        JTextPane updaterchangelog = new JTextPane();
        updaterscrollpane.setViewportView(updaterchangelog);

        updaterchangelog.setEditable(false);

        JLabel updaterchangeloglabel = new JLabel(PublicValues.language.translate("updater.dialog.changelog.label"));
        updaterchangeloglabel.setBounds(0, 11, 186, 14);
        contentPane.add(updaterchangeloglabel);

        updaterchangelog.setText(new Updater().getChangelogForNewest());

        JButton updaterupdatebutton = new JButton(PublicValues.language.translate("updater.dialog.download"));
        updaterupdatebutton.setBounds(0, 272, 249, 34);
        contentPane.add(updaterupdatebutton);

        updaterupdatebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DoubleArrayList updater = new Updater().updateAvailable();
                String version = ((GitHubAPI.Release)updater.getSecond(0)).version;
                try (BufferedInputStream in = new BufferedInputStream(new URL(((GitHubAPI.Release) updater.getSecond(0)).downloadURL).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream("SpotifyXP.jar")) {
                    byte[] dataBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                } catch (IOException e2) {
                    ExceptionDialog.open(e2);
                    ConsoleLogging.Throwable(e2);
                }
            }
        });

        JButton updatercancelbutton = new JButton(PublicValues.language.translate("updater.dialog.cancel"));
        updatercancelbutton.setBounds(247, 272, 249, 34);
        contentPane.add(updatercancelbutton);

        updatercancelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        this.setPreferredSize(new Dimension(512, 345));

        this.getContentPane().add(contentPane);

        this.setVisible(true);

        this.pack();
    }
}
