package com.spotifyxp.dialogs;

import com.spotifyxp.swingextension.JFrame;

import javax.swing.*;
import java.awt.*;

/**
 * A JFrame that shows one button [Cancel Login]<br>
 * When the button is clicked the Java process will exit with code 0
 */
public class CancelDialog extends JPanel {
    final JFrame frame = new JFrame("Cancel login?");
    final JButton b = new JButton("Cancel Login");
    public CancelDialog() {
        setLayout(null);
        setPreferredSize(new Dimension(300, 100));
        add(b, BorderLayout.CENTER);
        b.setBounds(0, 0, 300, 100);
    }

    /**
     * Displays the JFrame
     */
    public void showIt() {
        frame.getContentPane().add(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        b.addActionListener(e -> System.exit(0));
    }

    /**
     * Closes the JFrame
     */
    public void closeIt() {
        frame.dispose();
    }
}
