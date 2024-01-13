package com.spotifyxp.dialogs;

import com.spotifyxp.swingextension.JFrame;

import javax.swing.*;
import java.awt.*;

public class CancelDialog extends JPanel {
    final JFrame frame = new JFrame("Cancel login?");
    final JButton b = new JButton("Cancel Login");
    public CancelDialog() {
        setLayout(null);
        setPreferredSize(new Dimension(300, 100));
        add(b, BorderLayout.CENTER);
        b.setBounds(0, 0, 300, 100);
    }

    public void showIt() {
        frame.getContentPane().add(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        b.addActionListener(e -> System.exit(0));
    }

    public void closeIt() {
        frame.dispose();
    }
}
