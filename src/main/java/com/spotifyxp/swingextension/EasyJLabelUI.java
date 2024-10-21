package com.spotifyxp.swingextension;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EasyJLabelUI extends JFrame {
    private static final ArrayList<JLabel> labels = new ArrayList<>();
    private static JPanel panel;

    public EasyJLabelUI() {
        panel = new JPanel();
        JScrollPane pane = new JScrollPane(panel);
        panel.setLayout(null);
        add(pane, BorderLayout.CENTER);
    }

    public void addJLabel(JLabel label) {
        if (isVisible()) return;
        labels.add(label);
    }

    @Override
    public void open() {
        int ycache = 8;
        for (JLabel label : labels) {
            label.setBounds(5, ycache, getPreferredSize().width, 15);
            panel.add(label);
            ycache += 20 + 3;
            panel.setPreferredSize(new Dimension(getPreferredSize().width, ycache + 10));
        }
        super.open();
    }
}
