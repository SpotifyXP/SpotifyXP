package com.spotifyxp.dialogs;

import com.spotifyxp.utils.GraphicalMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CancelDialog extends JPanel {
    JFrame frame = new JFrame("Cancel login?");
    JButton b = new JButton("Cancel Login");
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
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void closeIt() {
        frame.dispose();
    }
}
