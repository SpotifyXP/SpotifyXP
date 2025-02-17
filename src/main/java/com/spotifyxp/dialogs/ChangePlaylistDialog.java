package com.spotifyxp.dialogs;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.se.michaelthelin.spotify.model_objects.specification.Playlist;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.swingextension.JDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Field;

public class ChangePlaylistDialog extends JDialog {
    public JButton okButton;
    public JButton cancelButton;
    public JTextField playlistName;
    public JLabel playlistNameLabel;
    public JLabel playlistDescriptionLabel;
    public JRadioButton visibility;
    public JRadioButton collaborative;
    public JTextArea playlistDescription;
    public JPanel contentPanel;

    public ChangePlaylistDialog() throws IOException {
        super();
        setModal(true);
        setContentPane(contentPanel);
        getRootPane().setDefaultButton(okButton);

        playlistNameLabel.setForeground(PublicValues.globalFontColor);
        playlistNameLabel.setText(PublicValues.language.translate("changeplaylist.name"));

        playlistDescriptionLabel.setForeground(PublicValues.globalFontColor);
        playlistDescriptionLabel.setText(PublicValues.language.translate("changeplaylist.description"));

        visibility.setForeground(PublicValues.globalFontColor);
        visibility.setText(PublicValues.language.translate("changeplaylist.visibility"));
        visibility.addChangeListener(e -> {
            if (visibility.isSelected()) {
                collaborative.setSelected(false);
            }
        });

        collaborative.setForeground(PublicValues.globalFontColor);
        collaborative.setText(PublicValues.language.translate("changeplaylist.collaborative"));
        collaborative.addChangeListener(e -> {
            if (collaborative.isSelected()) {
                visibility.setSelected(false);
            }
        });

        okButton.setText(PublicValues.language.translate("changeplaylist.ok"));

        cancelButton.setText(PublicValues.language.translate("changeplaylist.cancel"));
    }

    public static class ChangedPlaylist {
        public final String playlistName;
        public final String playlistDescription;
        public final boolean isPublic;
        public final boolean isCollaborative;

        ChangedPlaylist(String playlistName, String playlistDescription, boolean isPublic, boolean isCollaborative) {
            this.playlistName = playlistName;
            this.playlistDescription = playlistDescription;
            this.isPublic = isPublic;
            this.isCollaborative = isCollaborative;
        }
    }

    @FunctionalInterface
    public interface ChangedPlaylistRunnable {
        void receive(ChangedPlaylist playlist);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), -1, -1));
        cancelButton = new JButton();
        cancelButton.setText("");
        contentPanel.add(cancelButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        okButton = new JButton();
        okButton.setText("");
        contentPanel.add(okButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        playlistNameLabel = new JLabel();
        playlistNameLabel.setText("");
        panel1.add(playlistNameLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playlistName = new JTextField();
        panel1.add(playlistName, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        playlistDescriptionLabel = new JLabel();
        playlistDescriptionLabel.setText("");
        panel1.add(playlistDescriptionLabel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel1.add(separator1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        visibility = new JRadioButton();
        visibility.setText("");
        panel1.add(visibility, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        collaborative = new JRadioButton();
        collaborative.setSelected(false);
        collaborative.setText("");
        panel1.add(collaborative, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        playlistDescription = new JTextArea();
        scrollPane1.setViewportView(playlistDescription);
        final JSeparator separator2 = new JSeparator();
        contentPanel.add(separator2, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

    public void show(Playlist playlist, ChangedPlaylistRunnable runnable) {
        playlistName.setText(playlist.getName());
        playlistDescription.setText(playlist.getDescription());
        visibility.setSelected(playlist.getIsPublicAccess());
        collaborative.setSelected(playlist.getIsCollaborative());
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.receive(new ChangedPlaylist(
                        playlistName.getText(),
                        playlistDescription.getText(),
                        visibility.isSelected(),
                        collaborative.isSelected()
                ));
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setModal(true);
        setTitle(PublicValues.language.translate("changeplaylist.title"));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        pack();
        setVisible(true);
    }
}
