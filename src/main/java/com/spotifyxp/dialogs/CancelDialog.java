package com.spotifyxp.dialogs;

import com.spotifyxp.panels.ContentPanel;

import javax.swing.*;
import java.awt.*;

public class CancelDialog extends JDialog {
    private JPanel contentPane;
    private JButton cancelButton;

    public CancelDialog() {
        setPreferredSize(new Dimension(300, 100));
        setLocation(ContentPanel.frame.getCenter());
        setResizable(false);
        setContentPane(contentPane);
        setModal(true);
    }

    public void showIt() {
        cancelButton.setText("Cancel Login"); //ToDo: Translate
        setTitle("Cancel login?"); //ToDo: Translate
        pack();
        setVisible(true);
        cancelButton.addActionListener(e -> System.exit(0));
    }

    public void closeIt() {
        dispose();
    }

    public static void main(String[] args) {
        new CancelDialog().showIt();
    }
}
