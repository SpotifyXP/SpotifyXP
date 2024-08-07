package com.spotifyxp.dialogs;

import com.spotifyxp.PublicValues;
import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddPlaylistDialog extends JDialog {
    private JPanel contentPane;
    private JButton okbutton;
    private JButton cancelbutton;
    private JTextField playlistname;
    private JCheckBox playlistvisibility;
    private JLabel playlistnamelabel;

    @FunctionalInterface
    public interface OkRunnable {
        void run(String playlistname, boolean playlistvisibility);
    }

    public AddPlaylistDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(okbutton);
    }

    public void show(OkRunnable ok, Runnable cancel, Runnable onClose) {
        setLocation(ContentPanel.frame.getCenter());
        playlistvisibility.setText(PublicValues.language.translate("playlists.create.visibility"));
        playlistnamelabel.setText(PublicValues.language.translate("playlists.create.name.label"));
        setTitle(PublicValues.language.translate("playlists.create.title"));
        okbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok.run(playlistname.getText(), playlistvisibility.isSelected());
                onClose.run();
            }
        });
        cancelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                cancel.run();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose.run();
                dispose();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(800 / 2, 600 / 3));
        setResizable(false);
        pack();
        setVisible(true);
    }
}
